package ns.sos.domain.region.sido.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "시도 목록 정보 DTO")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SidoResponses {

    @Schema(description = "시도 정보 목록", example = "서울특별시, 대구광역시, 대전광역시")
    private List<SidoResponse> sidoResponse;

    public static SidoResponses from(final List<SidoResponse> response) {
        return new SidoResponses(response);
    }
}
