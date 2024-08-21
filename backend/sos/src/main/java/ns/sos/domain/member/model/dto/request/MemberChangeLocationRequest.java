package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "회원 위치정보 수정 request dto", description = "회원의 위치 정보를 수정합니다.")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberChangeLocationRequest {

    @Schema(description = "시도", example = "서울특별시")
    private String sido;

    @Schema(description = "구군", example = "광진구")
    private String gugun;

}
