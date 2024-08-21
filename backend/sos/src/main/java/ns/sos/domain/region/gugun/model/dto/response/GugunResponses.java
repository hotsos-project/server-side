package ns.sos.domain.region.gugun.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "구군 목록 정보 DTO")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GugunResponses {

    @Schema(description = "구군 정보 목록", example = "강남구, 강동구")
    private List<GugunResponse> gugunResponse;

    public static GugunResponses from(final List<GugunResponse> guguns) {
        return new GugunResponses(guguns);
    }
}
