package ns.sos.domain.follow.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class FollowFirebaseInfo {

    @NotEmpty
    private String memberLoginId;

    @NotEmpty
    private String followerLoginId;

    @NotEmpty
    private String isFollow;
}
