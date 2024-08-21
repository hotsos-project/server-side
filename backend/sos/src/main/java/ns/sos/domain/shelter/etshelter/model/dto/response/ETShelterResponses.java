package ns.sos.domain.shelter.etshelter.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "ETShelter 조회 responses dto", description = "ETShelter 조회 후 반환되는 여러개 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ETShelterResponses {

    @Schema(description = "ETShelter 정보들", example = "ETShelter 정보들")
    private final List<ETShelterResponse> etShelterResponses;

    public static ETShelterResponses from(final List<ETShelterResponse> etShelterResponses) {
        return new ETShelterResponses(etShelterResponses);
    }
}