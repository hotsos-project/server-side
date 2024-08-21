package ns.sos.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.member.model.dto.Member;

@Schema(name = "회원 조회 Info", description = "회원 조회 정보")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberSearchInfo {

    @Schema(description = "회원 ID", example = "24")
    private int id;

    @Schema(description = "로그인 ID", example = "john_doe")
    private String loginId;

    @Schema(description = "닉네임", example = "johnny")
    private String nickname;

    public static MemberSearchInfo from(Member member) {
        return new MemberSearchInfo(member.getId(), member.getLoginId(), member.getNickname());
    }
}
