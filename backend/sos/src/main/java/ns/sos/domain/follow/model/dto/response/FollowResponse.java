package ns.sos.domain.follow.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.global.util.RedisUtil;

@Schema(description = "팔로우 응답 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowResponse {

    @Schema(description = "팔로워 ID", example = "john_doe")
    private Integer id;

    @Schema(description = "회원 로그인 ID", example = "john_doe")
    private String memberId;

    @Schema(description = "회원 닉네임", example = "마이쮸킬러")
    private String nickName;

    @Schema(description = "안전상태 true 면 버튼 누른거 false면 무응답", example = "true, false")
    private boolean save;

    public static FollowResponse of(final Integer id, final Member member, final boolean safe) {
        return new FollowResponse(id, member.getLoginId(), member.getNickname(), safe);
    }

    public static FollowResponse of(final Integer id, final Member member, final String nickName, final boolean safe) {
        return new FollowResponse(id, member.getLoginId(), nickName, safe);
    }
}
