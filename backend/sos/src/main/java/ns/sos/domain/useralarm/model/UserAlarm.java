package ns.sos.domain.useralarm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.alarm.model.Alarm;
import ns.sos.domain.member.model.dto.Member;

@Entity
@Table(name = "user_alarm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "alarm_id", nullable = false)
    private Alarm alarm;

    @NotBlank
    char isRead = 'N';

    private UserAlarm(final Member member, final Alarm alarm) {
        this.member = member;
        this.alarm = alarm;
    }

    public static UserAlarm of(final Member member, final Alarm alarm) {
        return new UserAlarm(member, alarm);
    }
}
