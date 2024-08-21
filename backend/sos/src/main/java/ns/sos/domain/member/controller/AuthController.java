package ns.sos.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.model.dto.request.*;
import ns.sos.domain.member.model.dto.response.MemberLoginResponse;
import ns.sos.domain.member.model.dto.response.MemberReissueTokenResponse;
import ns.sos.domain.member.service.AuthService;
import ns.sos.domain.member.service.MemberSmsService;
import ns.sos.global.config.security.oauth.OauthUserService;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "인증 관련 API (로그인, 회원가입)")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final OauthUserService oauthUserService;
    private final AuthService authService;
    private final MemberSmsService smsUserService;

    /*
    * sms 인증 처리
    * */

    @Operation(summary = "SMS 인증번호 전송 API", description = "전화번호로 SMS 인증번호를 전송합니다.")
    @SwaggerApiSuccess(description = "SMS 인증번호 전송 성공")
    @SwaggerApiError({ErrorCode.INVALID_PHONE_NUMBER})
    @PostMapping("/sms/send")
    public Response<?> sendSms(@RequestBody MemberPhoneRequest requestDto) {
        smsUserService.sendSms(requestDto);
        return Response.SUCCESS();
    }

    @Operation(summary = "SMS 인증번호 확인 API", description = "사용자가 입력한 인증번호를 확인합니다.")
    @SwaggerApiSuccess(description = "SMS 인증번호 확인 성공")
    @SwaggerApiError({ErrorCode.INVALID_CERTIFICATION_NUMBER})
    @PostMapping("/sms/verify")
    public Response<?> verifySms(@RequestBody MemberSmsRequest requestDto) {
        smsUserService.verifySms(requestDto);
        return Response.SUCCESS();
    }

    /**
     * 로그인 요청을 처리
     *
     * @param memberLoginRequest 로그인 요청 데이터
     * @return 로그인 응답 데이터 (JWT 토큰)
     */

    @Operation(summary = "로그인 API", description = "MemberLoginRequest로 로그인 진행")
    @SwaggerApiSuccess(description = "로그인 성공")
    @SwaggerApiError({ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_MATCH_PASSWORD})
    @PostMapping("/login")
    public Response<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest memberLoginRequest) {
        MemberLoginResponse memberLoginResponse = authService.login(memberLoginRequest);
        return Response.SUCCESS(memberLoginResponse, "login을 성공했습니다.");
    }

    @Operation(summary = "회원가입 API", description = "MemberCreateRequest로 회원가입 진행")
    @SwaggerApiSuccess(description = "회원가입 성공")
    @SwaggerApiError({ErrorCode.DUPLICATED_MEMBER})
    @PostMapping("/register")
    public Response<?> register(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        authService.register(memberCreateRequest);
        return Response.SUCCESS();
    }

    /**
     * Test용.
     * kakao 로그인 요청. 인증 코드를 받아 해당 uri로 redirect 시킵니다.
     *
     * @return
     */
    //todo : 클라이언트에서 인증 코드를 받아서 준다면 해당 api는 test 시에만 사용.
    @Operation(hidden = true)
    @GetMapping("/oauth2/login")
    public ResponseEntity<String> login() {
        URI redirectUri = URI.create(oauthUserService.getAuthorizationUri("kakao"));
//        log.info("redirectUri: {}", redirectUri.toString());
//        return ResponseEntity.status(HttpStatus.OK).body(redirectUri.toString());
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    /**
     * Test용.
     * 브라우저에서 /oauth2/login으로 접속 시 리다이렉션으로 해당 url로 이동한다.
     * @param code
     * @return
     */
    @Operation(hidden = true)
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<String> oauthKakaoLoginTest(@RequestParam("code") String code) {
        log.info("get token controller 진입");
        String accessToken = oauthUserService.getAccessToken("kakao", code);
        log.info("accessToken: {}", accessToken);
        String email = oauthUserService.getEmail(accessToken);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @Operation(hidden = true)
    @GetMapping("/oauth2/getcode")
    public ResponseEntity<String> getKakaoCode() {
        String code = oauthUserService.getCode();
        log.info("code: {}", code);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @Operation(hidden = true)
    @GetMapping("/favicon.io")
    public ResponseEntity<String> getFavicon() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @Operation(hidden = true)
    @GetMapping("/favicon.ico")
    public ResponseEntity<String> getFaviconIco() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /**
     * 인가코드를 받아 kakao accessToken을 받고, 로그인 처리를 합니다.
     * @param code
     * @return
     */
    @Operation(summary = "Kakao 로그인 API", description = "kakao 인증 code로 로그인 진행")
    @SwaggerApiSuccess(description = "kakao 로그인 성공")
    @SwaggerApiError({ErrorCode.NOT_EXIST_MEMBER})
    @GetMapping("/login/oauth")
    public Response<MemberLoginResponse> oauthKakaoLogin(@RequestParam("code") String code) {
        String accessToken = oauthUserService.getAccessToken("kakao", code);
        String email = oauthUserService.getEmail(accessToken);
        MemberLoginResponse response = authService.oauthLogin(email);
        return Response.SUCCESS(response, "kakao login을 성공했습니다.");
    }

    @Operation(summary = "access token reissue API", description = "refresh token과 access token을 받아 새로운 access token을 발급합니다.")
    @SwaggerApiSuccess(description = "reissue 성공")
    @SwaggerApiError({ErrorCode.EXPIRED_JWT_TOKEN})
    @PostMapping("/reissue")
    public Response<MemberReissueTokenResponse> reissue(@RequestBody MemberReissueRequest request) {
        MemberReissueTokenResponse response = authService.reissueToken(request);
        return Response.SUCCESS(response, "access token을 재발급 했습니다.");
    }

    @GetMapping("/test")
    public Response<?> test(){
        return Response.SUCCESS();
    }

    @Operation(summary = "login id 중복체크 API", description = "login id가 중복이면 true, 중복이 아니면 false를 반환합니다.")
    @GetMapping("/register/duplicate")
    public Response<Boolean> registerValidateDuplicate(@RequestParam("loginId") String loginId) {
        return Response.SUCCESS(authService.isDuplicateId(loginId));
    }
}
