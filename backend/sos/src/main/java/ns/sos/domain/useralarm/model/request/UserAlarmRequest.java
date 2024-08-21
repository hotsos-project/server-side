package ns.sos.domain.useralarm.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.alarm.model.Alarm;
import ns.sos.domain.member.model.dto.Member;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class UserAlarmRequest {
    private Member member;
    private Alarm alarm;
    private String fcmToken;
}
