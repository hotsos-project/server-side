package ns.sos.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //1. OAuth 예외
        // - kakao login 실패
        // - kakao 통신 실패
        // - 만료된 access token
        // - 서비스 유저가 아님 (회원가입 필요)

        //2. 일반 로그인 예외
        // - 회원 정보 일치하지 않음 (비번틀림)

        //todo : 추후 공통 response 형식이 확정된다면 해당 형식으로 변경 필요
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> responseBody = new HashMap<>();

        if(exception instanceof OAuth2AuthenticationException){
            handleOAuth2AuthenticationException((OAuth2AuthenticationException) exception, responseBody);
        }
        else{ //todo : 일반 로그인 예외처리 필요
            responseBody.put("error", exception.getMessage());
        }

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
        writer.close();
    }

    private void handleOAuth2AuthenticationException(OAuth2AuthenticationException exception, Map<String, String> responseBody) throws IOException {
        String errorCode = exception.getError().getErrorCode();

        //todo : error code 생성되면 errorcode로 비교 필요, 추후 공통 response 형식이 확정된다면 해당 형식으로 변경 필요
        if(errorCode.equals("signup_required")){
            responseBody.put("message", "회원가입이 필요합니다.");
        }
        else{
            responseBody.put("message", "알 수 없는 OAuth2 오류");
        }

        responseBody.put("redirect", "/path");
    }
}
