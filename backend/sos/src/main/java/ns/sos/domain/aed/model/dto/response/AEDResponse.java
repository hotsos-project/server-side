package ns.sos.domain.aed.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.aed.model.AED;

@Schema(name = "AED 조회 response dto", description = "AED 조회 후 반환되는 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AEDResponse {

    @Schema(description = "AED id" , example = "1")
    private Integer id;

    @Schema(description = "시 정보", example = "수원시")
    private String sido;

    @Schema(description = "구군 정보", example = "권선구")
    private String gugun;

    @Schema(description = "주소 디테일 정보", example = "권선로357번길 30")
    private String detailAddress;

    @Schema(description = "설치 장소", example = "2층 비상구 앞")
    private String buildPlace;

    @Schema(description = "위도", example = "32.1230213")
    private double lat;

    @Schema(description = "경도", example = "14.1231242")
    private double lon;

    @Schema(description = "번호", example = "010-1234-5678")
    private String tel;

    @Schema(description = "월요일시간", example = "00002400")
    private String monTime;

    @Schema(description = "화요일시간", example = "00002400")
    private String tueTime;

    @Schema(description = "수요일시간", example = "00002400")
    private String wedTime;
    
    @Schema(description = "목요일시간", example = "00002400")
    private String thuTime;
    
    @Schema(description = "금요일시간", example = "00002400")
    private String friTime;
    
    @Schema(description = "주말시간", example = "00002400")
    private String holTime;

    public static AEDResponse from(final AED aed) {
        return new AEDResponse(
                aed.getId(),
                aed.getSido(),
                aed.getGugun(),
                aed.getDetailAddress(),
                aed.getBuildPlace(),
                aed.getLat(),
                aed.getLon(),
                aed.getTel(),
                aed.getMonTime(),
                aed.getTueTime(),
                aed.getWedTime(),
                aed.getThuTime(),
                aed.getFriTime(),
                aed.getHolTime());
    }
}
