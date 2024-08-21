package ns.sos.domain.favoriteregion.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class FavoriteRegionFirebaseInfo {

    @NotEmpty
    private String favoriteSido;

    @NotEmpty
    private String favoriteGugun;

    @NotEmpty
    private String userLoginId;

    @NotEmpty
    private String isAdd;
}
