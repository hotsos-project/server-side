package ns.sos.domain.alarm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.global.common.BaseEntity;

@Entity
@Table(name = "alarm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AlarmType type;

    //todo: type에 따른 id 값(외래키)
    private Integer keyId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private Sido sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gugun_id")
    private Gugun gugun;

    private Alarm(final AlarmType type, final Integer keyId, final String title, final String content) {
        this.type = type;
        this.keyId = keyId;
        this.title = title;
        this.content = content;
    }

    private Alarm(final AlarmType type, final Integer keyId, final String title, final String content, final Sido sido, final Gugun gugun) {
        this.type = type;
        this.keyId = keyId;
        this.title = title;
        this.content = content;
        this.sido = sido;
        this.gugun = gugun;
    }

    public static Alarm of(final AlarmType type, final Integer keyId, final String title, final String content) {
        return new Alarm(type, keyId, title, content);
    }

    public static Alarm of(final AlarmType type, final Integer keyId, final String title, final String content, final Sido sido, final Gugun gugun) {
        return new Alarm(type, keyId, title, content, sido, gugun);
    }

    public static Alarm of(AlarmInfo alarmInfo, Integer keyId, Sido sido, Gugun gugun) {
        return new Alarm(AlarmType.getAlarmType(alarmInfo.getAlarmType()), keyId, alarmInfo.getTitle(), alarmInfo.getContent(), sido, gugun);
    }

    public static Alarm from(AlarmInfo alarmInfo, Integer keyId) {
        return new Alarm(AlarmType.valueOf(alarmInfo.getAlarmType()), keyId, alarmInfo.getTitle(), alarmInfo.getContent());
    }

    public static Alarm of(CommentAlarmInfo alarmInfo, Integer keyId) {
        return new Alarm(AlarmType.getAlarmType(alarmInfo.getAlarmType()), keyId, alarmInfo.getTitle(), alarmInfo.getContent());
    }
}
