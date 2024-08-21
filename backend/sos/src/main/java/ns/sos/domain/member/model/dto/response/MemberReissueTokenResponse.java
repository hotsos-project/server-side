package ns.sos.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(name = "reissue response dto", description = "reissue 후 반환되는 token 정보")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberReissueTokenResponse {

    @Schema(description = "access token", example = "token.token.token")
    private final String accessToken;

    public static MemberReissueTokenResponse from(String accessToken){
        return new MemberReissueTokenResponse(accessToken);
    }
}
