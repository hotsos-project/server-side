package ns.sos.global.config.security.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.global.config.security.PrincipalDetails;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final WebClient.Builder webClientBuilder;

    public String getAuthorizationUri(String registrationId) {
        //todo : URL을 @Value로 받아야 한다. (application.yml에 넣기)
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientRegistration.getClientId() +
                "&redirect_uri=" + clientRegistration.getRedirectUri() +
                "&response_type=code";
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser 진입");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        //OAuth 공급자(KAKAO)로부터 Access token(사용자 정보)까지 가져온 상태

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        //todo : 로그인 실패 시 Redirect 처리 - 공통 response에 Redirect 추가
        //todo : 에러코드 구현 시 에러코드를 넣어 보내고 코드로 구분해서 handler 작성 필요
        Member member = memberRepository.findByLoginIdAndStatus(email,'Y')
                .orElseThrow(()-> new OAuth2AuthenticationException("signup_required"));

        //OAuth2User의 구현체인 PrincipalDetails 반환
        return new PrincipalDetails(member.getId(), member.getRole(), oAuth2User.getAttributes());
    }

    /**
     * 인증 코드를 발급받는다.
     * @return
     */
    public String getCode(){
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");

        //todo : Webclient Requester 구현 필요
        WebClient webClient = webClientBuilder.build();

        String uri = "https://kauth.kakao.com/oauth/authorize";
        uri += "?client_id=" + clientRegistration.getClientId() +"&redirect_uri=" + clientRegistration.getRedirectUri()
                +"&response_type=code";

//        log.info("uri: {}", uri);
        String token = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        return token;
    }


    /**
     * 사용자에게 인증 코드를 받아 AccessToken을 발급받는다.
     * @param registrationId
     * @param code
     * @return
     */
    public String getAccessToken(String registrationId, String code){
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

//        log.info("get access token!!!");
        //todo : Webclient Requester 구현 필요
        WebClient webClient = webClientBuilder.build();

        String result = webClient.post()
                .uri(clientRegistration.getProviderDetails().getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientRegistration.getClientId())
                        .with("client_secret", clientRegistration.getClientSecret())
                        .with("redirect_uri", clientRegistration.getRedirectUri())
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class)
                .block();

//        log.info("result: {}", result);
        // todo : 에러 발생 시 예외처리 필요
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(result);
            return jsonNode.get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        String accessToken = map.get("access_token");
    }

    public String getEmail(String accessToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
        WebClient webClient = webClientBuilder.build();
//        log.info("get email!!");
//        log.info("uri: {}", clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri());
//        log.info("accessToken: {}", "Bearer " + accessToken);
        String result = webClient.post()
                .uri(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String email = "";
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(result);
            email = jsonNode.get("kakao_account").get("email").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        //todo : error 처리 필요
//        log.info("email: {}", email);
        return email;
    }
}
