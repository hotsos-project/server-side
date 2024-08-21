package ns.sos.global.config.guard;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import ns.sos.global.config.security.jwt.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Integer resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = jwtTokenProvider.resolveToken(webRequest.getHeader("Authorization"));
        Claims payload = jwtTokenProvider.getPayload(accessToken);
        return jwtTokenProvider.getUserIdFromPayload(payload);
    }
}
