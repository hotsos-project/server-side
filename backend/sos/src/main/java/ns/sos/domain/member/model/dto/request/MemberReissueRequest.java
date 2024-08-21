package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "reissue request dto", description = "reissue 시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberReissueRequest {

    @Schema(description = "access token", example = "token.token.token")
    @NotEmpty(message = "access token을 입력하세요")
    private String accessToken;

    @Schema(description = "refresh token", example = "token.token.token")
    @NotEmpty(message = "refresh token을 입력하세요")
    private String refreshToken;
}
