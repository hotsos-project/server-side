package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "문자 request dto", description = "회원가입시 문자인증 필요")
@Getter
public class MemberSmsRequest {

    @Schema(description = "전화번호", example = "01011112222")
    private String phone;

    @Schema(description = "인증번호", example = "1234")
    private String certificationNumber;

}
