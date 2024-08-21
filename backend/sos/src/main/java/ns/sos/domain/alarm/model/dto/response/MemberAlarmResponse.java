package ns.sos.domain.alarm.model.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.global.util.StringArrayListConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Schema(name = "사용자 알람 list dto", description = "사용자 알람 list를 나타냅니다.")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberAlarmResponse {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Schema(description = "alarm id", example = "12")
    private Integer id;

    @Schema(description = "alarm type", example = "hot_issue / comment")
    private String alarmType;

    @Schema(description = "user alarm id", example = "kaWlkajdfhwoeKdd")
    private String userAlarmId;

    @Schema(description = "연결된 게시판 id", example = "12")
    private Integer boardId;

    @Schema(description = "알람 제목", example = "제목1")
    private String title;

    @Schema(description = "알람 내용", example = "내용1")
    private String content;

    @Schema(description = "생성 시간", example = "2024-07-25 17:51:02")
    private String createdTime;

    @Schema(description = "읽음 여부", example = "true")
    private boolean isRead;

    public static MemberAlarmResponse fromJson(JsonNode alarmNode) {
        String isReadText = alarmNode.get("isRead").asText();
        Boolean isRead = false;
        if(isReadText.equals("Y")) {
            isRead = true;
        }
        String alarmTypeText = alarmNode.get("alarmType").asText();
        String title = "";
        String content = "";
        String textList = alarmNode.get("textList").asText().replace("[", "").replace("]", "");
        List<String> tokens = StringArrayListConverter.convertStringToList(textList);
        if (alarmTypeText.equals("comment")) {
            title = "[댓글 알람]";
            content = tokens.get(0) + "님이 " + tokens.get(1) + " 게시글에 댓글을 남겼습니다.";
        } else if (alarmTypeText.equals("hot_issue")) {
            // 제목 생성
            title = String.format("[핫이슈] %s %s", tokens.get(0), tokens.get(1));

            // 내용 생성
            content = String.format("%s에 %s 유형의 %s 사고가 일어났습니다.", tokens.get(0), tokens.get(1), tokens.get(2));

        }
        return new MemberAlarmResponse(
                alarmNode.get("alarmId").asInt(),
                alarmNode.get("alarmType").asText(),
                alarmNode.get("id").asText(),
                alarmNode.get("keyId").asInt(),
                title,
                content,
                alarmNode.get("sendTime").asText(),
                isRead
        );
    }
}
