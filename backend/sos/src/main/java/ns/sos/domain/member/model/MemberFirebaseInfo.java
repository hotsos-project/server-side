package ns.sos.domain.member.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberFirebaseInfo {

    @NotEmpty
    private String memberLoginId;

    @NotEmpty
    private String fcmToken;

    @NotEmpty
    private String sido;

    @NotEmpty
    private String gugun;
}
