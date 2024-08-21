package ns.sos.domain.shelter.eoshelter.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.shelter.eoshelter.model.EOShelter;

@Schema(name = "EOShelter 조회 response dto", description = "EOShelter 조회 후 반환되는 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EOShelterResponse {

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

    @Schema(description = "위도", example = "32.1230213")
    private double lat;

    @Schema(description = "경도", example = "14.1231242")
    private double lon;

    @Schema(description = "번호", example = "010-1234-5678")
    private String tel;

    public static EOShelterResponse from(final EOShelter eoShelter) {
        return new EOShelterResponse(
                eoShelter.getId(),
                eoShelter.getSido(),
                eoShelter.getGugun(),
                eoShelter.getDetailAddress(),
                eoShelter.getName(),
                eoShelter.getLat(),
                eoShelter.getLon(),
                eoShelter.getTel());
    }
}