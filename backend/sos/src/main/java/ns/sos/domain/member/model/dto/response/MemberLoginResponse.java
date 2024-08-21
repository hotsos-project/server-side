package ns.sos.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.global.config.security.jwt.JwtToken;

@Schema(name = "로그인 response dto", description = "로그인 후 반환되는 token 정보")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberLoginResponse {

    @Schema(description = "access token", example = "token.token.token")
    private String accessToken;

    @Schema(description = "refresh token", example = "token.token.token")
    private String refreshToken;

    public static MemberLoginResponse of(String accessToken, String refreshToken){
        return new MemberLoginResponse(accessToken, refreshToken);
    }

    public static MemberLoginResponse from(JwtToken jwtToken){
        return new MemberLoginResponse(jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }
}
