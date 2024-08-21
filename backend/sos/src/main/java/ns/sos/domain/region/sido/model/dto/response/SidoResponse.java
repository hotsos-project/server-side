package ns.sos.domain.region.sido.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "시도 정보 DTO")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SidoResponse {

    @Schema(description = "시도 ID", example = "1")
    private Integer id;

    @Schema(description = "시도 이름", example = "서울특별시")
    private String name;

    public static SidoResponse of(final Integer id, final String name) {
        return new SidoResponse(id, name);
    }
}
