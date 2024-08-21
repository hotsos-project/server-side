package ns.sos.domain.alarm.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "알림 request dto", description = "알림 작성시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AlarmGetRequest {

    @Schema(description = "알람 정보", example = " ex) 재난, 게시판")
    @NotEmpty
    private String type;

    @Schema(description = "시도 정보", example = "수원시")
    private String sido;

    @Schema(description = "구군 정보", example = "팔달구")
    private String gugun;

    @Schema(description = "마지막 알람 id", example = "1")
    private Integer lastAlarmId;

    @Schema(description = "가져올 페이지 수", example = "10")
    private Integer limitPage;
}
