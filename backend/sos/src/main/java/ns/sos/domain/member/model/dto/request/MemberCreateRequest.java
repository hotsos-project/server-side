package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "회원가입 request dto", description = "회원가입 시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberCreateRequest {

    @Schema(description = "아이디", example = "asdf")
    @NotEmpty(message = "아이디를 입력하세요")
    private String loginId;

    @Schema(description = "비밀번호", example = "asdf")
    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String password;

    @Schema(description = "이름", example = "이름")
    @NotEmpty(message = "이름을 입력하세요.")
    private String name;

    @Schema(description = "닉네임", example = "닉네임")
    @NotEmpty(message = "닉네임을 입력하세요.")
    private String nickname;

    @Schema(description = "전화번호 인증 여부", example = "true")
    @NotNull(message = "전화번호를 인증하세요.")
    private boolean isCertifiedPhone;

    @Schema(description = "전화번호", example = "010-1234-5678")
    @NotEmpty(message = "전화번호를 입력하세요")
    private String phone;

    @Schema(description = "OAuth 종류", example = "NONE")
    @NotEmpty(message = "OAuth 종류를 입력하세요(NONE, KAKAO)")
    private String oauthType;

    @Schema(description = "FCM 토큰", example = "AljsdkfjAljsdkfjjfiwRfjdkjslfmldkmjfiwRfjdkjslfmldkm")
    @NotEmpty(message = "FCM 토큰을 입력하세요.")
    private String fcmToken;

    @Schema(description = "시도", example = "서울특별시")
    @NotEmpty(message = "시도를 입력하세요")
    private String sido;

    @Schema(description = "구군", example = "광진구")
    @NotEmpty(message = "구군을 입력하세요")
    private String gugun;
}
