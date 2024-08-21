package ns.sos.domain.alarm.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class CommentAlarmInfo {
    private String alarmType;

    private String loginId;

    private String title;

    private String content;

    private String keyword;

    private List<String> textList;

    private String keyId;
}
