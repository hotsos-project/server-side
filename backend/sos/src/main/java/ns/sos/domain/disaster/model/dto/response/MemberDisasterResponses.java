package ns.sos.domain.disaster.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "사용자 재난 문자 응답 목록 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDisasterResponses {

    @Schema(description = "사용자 재난 응답 목록")
    private final List<MemberDisasterResponse> disasterResponses;

    @Schema(description = "개수")
    private final int size;

    public static MemberDisasterResponses from(final List<MemberDisasterResponse> disasterResponses) {
        return new MemberDisasterResponses(disasterResponses, disasterResponses.size());
    }
}
