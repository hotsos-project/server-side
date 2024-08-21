package ns.sos.domain.member.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberLocationInfo {

    private String memberLoginId;

    private String sido;

    private String gugun;
}
