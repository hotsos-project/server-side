package ns.sos.domain.shelter.civilshelter.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.shelter.civilshelter.model.CivilShelter;

@Schema(name = "CivilShelter 조회 response dto", description = "CivilShelter 조회 후 반환되는 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CivilShelterResponse {

    @Schema(description = "대피소 id", example = "12")
    private Integer id;

    @Schema(description = "시 정보", example = "수원시")
    private String sido;

    @Schema(description = "구군 정보", example = "권선구")
    private String gugun;

    @Schema(description = "주소 디테일 정보", example = "권선로357번길 30")
    private String detailAddress;

    @Schema(description = "이름", example = "xx운동장")
    private String name;

    @Schema(description = "수용인원", example = "50")
    private int capacity;

    @Schema(description = "위도", example = "32.1230213")
    private double lat;

    @Schema(description = "경도", example = "14.1231242")
    private double lon;

    public static CivilShelterResponse from(final CivilShelter civilShelter) {
        return new CivilShelterResponse(
                civilShelter.getId(),
                civilShelter.getSido(),
                civilShelter.getGugun(),
                civilShelter.getDetailAddress(),
                civilShelter.getName(),
                civilShelter.getCapacity(),
                civilShelter.getLat(),
                civilShelter.getLon());
    }
}