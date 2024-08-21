package ns.sos.domain.favoriteregion.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ns.sos.domain.favoriteregion.model.FavoriteRegion;

@Schema(name = "관심지역 조회 dto", description = "관심지역 조회 DTO")
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteRegionResponse {

    @Schema(description = "관심지역 ID", example = "1")
    private Integer id;

    @Schema(description = "시도 ID", example = "1")
    private String sidoId;

    @Schema(description = "구군 ID", example = "1")
    private String gugun;

    public static FavoriteRegionResponse from(final FavoriteRegion favoriteRegion) {
        return new FavoriteRegionResponse(favoriteRegion.getId(), favoriteRegion.getSido().getName(), favoriteRegion.getGugun().getName());
    }
}
