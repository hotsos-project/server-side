package ns.sos.domain.alarm.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Schema(name = "알람 정보 list dto", description = "알람 정보 list를 나타냅니다.")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AlarmResponses {

    @Schema(description = "알람 Response list", example ="[\n" +
            "            {\n" +
            "                \"id\": 6,\n" +
            "                \"title\": \"Title 6\",\n" +
            "                \"content\": \"Content for alarm 6\",\n" +
            "                \"createdTime\": \"2024-07-25 17:51:02\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": 8,\n" +
            "                \"title\": \"Title 8\",\n" +
            "                \"content\": \"Content for alarm 8\",\n" +
            "                \"createdTime\": \"2024-07-25 17:51:02\"\n" +
            "            }]")
    private List<AlarmResponse> alarms;

    @Schema(description = "알람 Response size", example = "2")
    private int size;

    public static AlarmResponses from(List<AlarmResponse> alarms) {
        return new AlarmResponses(alarms, alarms.size());
    }
}
