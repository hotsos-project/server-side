package ns.sos.global.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.model.dto.Role;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.util.AES256SecureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Jwt Token util
 */

@Slf4j
@Component
public class JwtTokenProvider {
    // todo: application.yml 변경공유 (secret key, expiration time)
    private final SecretKey secretKey;
    private final String userInfoSecretKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private final AES256SecureUtil aes256SecureUtil;

    private final Set<String> invalidatedTokens = new HashSet<>();

    // todo: static 변수를 생성자를 통해 할당이 맞는지
    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey,
                            @Value("${spring.jwt.user-info-secret}") String userInfoSecretKey,
                            @Value("${spring.jwt.access_expiration_time}") long accessTokenExpireTime,
                            @Value("${spring.jwt.refresh_expiration_time}")long refreshTokenExpireTime){
        this.userInfoSecretKey = userInfoSecretKey;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)); // secretKey도 일반 문자열이 아닌 암호화된 문자열로
        this.aes256SecureUtil = new AES256SecureUtil(userInfoSecretKey);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    /**
     * accessToken의 내용으로 새로운 accessToken을 만듭니다.
     * @param accessToken
     * @return
     */
    public String reissue(String accessToken) {
        log.info("reissue");
        Claims payload = getPayload(accessToken);
        log.info("get payload 완료");
        Integer userId = getUserIdFromPayload(payload);
        Role role = getRoleFromPayload(payload);

        return createAccessToken(userId, role);
    }

    public JwtToken issue(int userId, Role role) {
        return new JwtToken(createAccessToken(userId, role), createRefreshToken());
    }

    //access token 생성
    public String createAccessToken(int userId, Role role){

        Date expireationDate = new Date(System.currentTimeMillis() + accessTokenExpireTime);
        String userIdEncrypt = aes256SecureUtil.encrypt(Integer.toString(userId), expireationDate.toString());
        String userRoleEncrypt = aes256SecureUtil.encrypt(role.toString(), expireationDate.toString());
//        log.info("userId: {}, role: {}", userIdEncrypt, userRoleEncrypt);
        Claims claims = Jwts.claims()
                .subject("UserInfo")
                .add("userId", userIdEncrypt)
                .add("role", userRoleEncrypt)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpireTime)).build();

        return Jwts.builder()
                .header()
                .keyId("KeyId")
                .and()
                .claims(claims)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
    //refresh token 생성
    public String createRefreshToken(){
        Claims claims = Jwts.claims()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime)).build();

        return Jwts.builder()
                .header()
                .keyId("KeyId")
                .and()
                .claims(claims)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출.
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Claims getPayload(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public int getUserIdFromPayload(final Claims claims) {
        String userId = claims.get("userId", String.class);
        return Integer.parseInt(aes256SecureUtil.decrypt(userId, claims.getExpiration().toString()));
    }

    public Role getRoleFromPayload(Claims claims) {
        String userRole = claims.get("role", String.class);
        return Role.valueOf(aes256SecureUtil.decrypt(userRole, claims.getExpiration().toString()));
    }

    /**
     * JWT 토큰의 유효성을 검증
     * @param token JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(final String token) {
        try {
//            Jwts.parserBuilder().
            //jwt가 위변조되지 않았는지 secretKey를 이용해 확인
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch(SecurityException | MalformedJwtException e){
            throw new CustomJwtException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            return false;
//            throw new CustomJwtException(ErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e){
            throw new CustomJwtException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            throw new CustomJwtException(ErrorCode.ILLEGAL_JWT_TOKEN);
        }
    }

    /**
     * 요청 헤더에서 JWT 토큰 추출
     * @param request HTTP 요청
     * @return JWT 토큰
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
//        if(bearerToken == null){
//            throw new CustomJwtException(ErrorCode.NULL_JWT_TOKEN);
//        }
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
//        throw new CustomJwtException(ErrorCode.INVALID_BEARER_JWT_TOKEN);
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 로그아웃 체크할려고 만든건데 이런식맞냐
    // 이렇게 해시셋안만들거면 레디스쓰라던데 일단 이렇게 만듬
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

}
