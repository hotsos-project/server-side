package ns.sos.domain.alarm.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "사용자 알람 정보 list dto", description = "사용자 알람 정보 list를 나타냅니다.")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberAlarmResponses {

    @Schema(description = "알람 Response list")
    private List<MemberAlarmResponse> alarms;

    @Schema(description = "알람 Response size", example = "2")
    private int size;

    public static MemberAlarmResponses from(List<MemberAlarmResponse> alarms) {
        return new MemberAlarmResponses(alarms, alarms.size());
    }
}
