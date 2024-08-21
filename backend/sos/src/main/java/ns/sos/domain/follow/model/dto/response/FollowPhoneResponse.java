package ns.sos.domain.follow.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.member.model.dto.Member;

@Schema(description = "팔로우 전화번호 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowPhoneResponse {

    @Schema(description = "팔로워 번호", example = "010-1234-7880")
    private String phone;

    public static FollowPhoneResponse from(final Member member) {
        return new FollowPhoneResponse(member.getPhone());
    }
}
