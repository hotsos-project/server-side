package ns.sos.domain.disaster.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.disaster.model.Disaster;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Schema(description = "재난 응답 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DisasterResponse {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Schema(description = "재난 id", example = "1")
    private Integer id;

    @Schema(description = "위치 이름", example = "서울시")
    private String locationName;

    @Schema(description = "메시지", example = "폭우 경고")
    private String msg;

    @Schema(description = "분류", example = "기상")
    private String classification;

    @Schema(description = "레벨", example = "심각")
    private String level;

    @Schema(description = "전송 시간", example = "2023-08-01T12:34:56")
    private String sendTime;

    @Schema(description = "일련 번호", example = "12345")
    private int serialNumber;

    public static DisasterResponse from(final Disaster disaster) {
        LocalDateTime localDateTime = disaster.getSendTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();


        return new DisasterResponse(
                disaster.getId(),
                disaster.getLocationName(),
                disaster.getMsg(),
                disaster.getClassification(),
                disaster.getLevel(),
                localDateTime.format(formatter),
                disaster.getSerialNumber());
    }
}
