package ns.sos.domain.region.gugun.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "구군 정보 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GugunResponse {

    @Schema(description = "구군 ID", example = "1")
    private Integer id;

    @Schema(description = "구군 이름", example = "강남구")
    private String name;

    public static GugunResponse of(final Integer id, final String name) {
        return new GugunResponse(id, name);
    }
}