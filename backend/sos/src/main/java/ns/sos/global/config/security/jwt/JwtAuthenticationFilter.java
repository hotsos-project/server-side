package ns.sos.global.config.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.model.dto.Role;
import ns.sos.global.config.security.PrincipalDetails;
import ns.sos.global.error.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * OncePerRequestFilter : 요청당 한 번만 실행되어야 하는 작업 시 사용
 * 모든 요청에 대해서 JWT를 검증함
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/register", "/login", "/reissue", "/login/oauth", "/oauth2/**", "/login/oauth2/**", "/error",
            "/login/oauth2/code/kakao", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/fcm/test",
            "/register/duplicate", "/sms/**", "/locations/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (isExcluded(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenProvider.resolveToken(request);
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if(!jwtTokenProvider.validateToken(token))
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT_TOKEN, "Access token이 만료됐습니다.");

        Claims payload = jwtTokenProvider.getPayload(token);
        int userId = jwtTokenProvider.getUserIdFromPayload(payload);
        Role role = jwtTokenProvider.getRoleFromPayload(payload);

        UserDetails userDetails = new PrincipalDetails(userId, role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isExcluded(String requestURI) {
        return EXCLUDE_URLS.stream().anyMatch(url -> antPathMatcher.match(url, requestURI));
    }
}
