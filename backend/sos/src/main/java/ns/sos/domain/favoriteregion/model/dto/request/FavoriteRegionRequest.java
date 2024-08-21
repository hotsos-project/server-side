package ns.sos.domain.favoriteregion.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "관심지역 request dto", description = "관심지역 request dto")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class FavoriteRegionRequest {

    @Schema(description = "시도 id", example = "1")
    @NotEmpty(message = "제목을 입력하세요.")
    private Integer sidoId;

    @Schema(description = "구군 id", example = "1")
    @NotEmpty(message = "제을 입력하세요.")
    private Integer gugunId;
}
