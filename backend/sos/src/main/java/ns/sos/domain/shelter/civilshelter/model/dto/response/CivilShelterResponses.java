package ns.sos.domain.shelter.civilshelter.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "CivilShelter 목록 조회 dto", description = "CivilShelter 조회 후 반환되는 여러개 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CivilShelterResponses {

    @Schema(description = "CivilShelter 정보들", example = "CivilShelter 정보들")
    private final List<CivilShelterResponse> civilShelterResponses;

    public static CivilShelterResponses from(final List<CivilShelterResponse> civilShelterResponses) {
        return new CivilShelterResponses(civilShelterResponses);
    }
}