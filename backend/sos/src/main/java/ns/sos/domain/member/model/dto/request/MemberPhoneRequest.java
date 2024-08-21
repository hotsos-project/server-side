package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "전화번호 request dto", description = "회원가입시 필요한 전화번호 요청")
@Getter
public class MemberPhoneRequest {

    @Schema(description = "전화번호", example = "01011112222")
    private String phone;
}
