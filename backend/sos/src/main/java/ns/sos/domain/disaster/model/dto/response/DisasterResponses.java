package ns.sos.domain.disaster.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "재난 응답 목록 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DisasterResponses {

    @Schema(description = "재난 응답 목록")
    private final List<DisasterResponse> disasterResponses;

    public static DisasterResponses from(final List<DisasterResponse> disasterResponses) {
        return new DisasterResponses(disasterResponses);
    }
}
