package ns.sos.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.member.model.dto.OAuthType;
import ns.sos.domain.member.model.dto.Role;

@Getter
@Schema(description = "회원 정보 응답 DTO")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoResponse {

    @Schema(description = "멤버 ID", example = "1")
    private Integer id;

    @Schema(description = "로그인 ID", example = "john_doe")
    private String loginId;

    @Schema(description = "이름", example = "John Doe")
    private String name;

    @Schema(description = "닉네임", example = "johnny")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "OAuth 타입")
    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;

    @Schema(description = "역할")
    @Enumerated(EnumType.STRING)
    private Role role;

    public static MemberInfoResponse of(final Integer id, final String loginId, final String name, final String nickname, final String phone, final OAuthType oauthType, final Role role){
        return new MemberInfoResponse(id, loginId, name, nickname, phone, oauthType, role);
    }
}
