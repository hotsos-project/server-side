package ns.sos.domain.follow.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "팔로우 요청 DTO")
@Getter
@AllArgsConstructor
public class FollowRequest {

    @Schema(description = "팔로우할 사용자 ID", example = "123")
    @NotNull
    private Integer followerId;
}
