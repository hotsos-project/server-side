package ns.sos.domain.alarm.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.alarm.model.Alarm;

import java.time.format.DateTimeFormatter;

@Schema(name = "알람 정보 dto", description = "알람 정보를 나타냅니다.")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AlarmResponse {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Schema(description = "alarm id", example = "12")
    private Integer id;

    @Schema(description = "알람 제목", example = "제목1")
    private String title;

    @Schema(description = "알람 내용", example = "내용1")
    private String content;

    @Schema(description = "생성 시간", example = "2024-07-25 17:51:02")
    private String createdTime;

    public static AlarmResponse from(Alarm alarm) {
        return new AlarmResponse(alarm.getId(), alarm.getTitle(), alarm.getContent(), alarm.getCreatedAt()
                .format(formatter));
    }

    public AlarmResponse(Alarm alarm) {
        this.id = alarm.getId();
        this.title = alarm.getTitle();
        this.content = alarm.getContent();
        this.createdTime = alarm.getCreatedAt().format(formatter);
    }
}
