package ns.sos.domain.follow.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "팔로우 응답 목록 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowResponses {

    @Schema(description = "팔로우 응답 목록")
    private final List<FollowResponse> followResponses;

    public static FollowResponses from(final List<FollowResponse> followResponses) {
        return new FollowResponses(followResponses);
    }
}
