package ns.sos.domain.aed.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "AED 조회 responses dto", description = "aed 조회 후 반환되는 여러개 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AEDResponses {

    @Schema(description = "AED 정보들", example = "AED 정보들")
    private final List<AEDResponse> aedResponses;

    public static AEDResponses from(final List<AEDResponse> aedResponses) {
        return new AEDResponses(aedResponses);
    }
}
