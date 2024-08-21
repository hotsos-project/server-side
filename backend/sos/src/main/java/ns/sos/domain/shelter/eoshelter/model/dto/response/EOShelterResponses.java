package ns.sos.domain.shelter.eoshelter.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "EOShelter 목록 조회 dto", description = "EOShelter 조회 후 반환되는 여러개 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EOShelterResponses {

    @Schema(description = "EOShelter 정보들", example = "EOShelter 정보들")
    private final List<EOShelterResponse> eoShelterResponses;

    public static EOShelterResponses from(final List<EOShelterResponse> eoShelterResponses) {
        return new EOShelterResponses(eoShelterResponses);
    }
}