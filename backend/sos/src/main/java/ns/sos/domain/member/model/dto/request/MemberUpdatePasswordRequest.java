package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "회원 비밀번호 수정 request dto", description = "회원정보 비밀번호 수정시 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberUpdatePasswordRequest {

    @Schema(description = "현재 비밀번호", example = "1111")
    @NotEmpty(message = "현재 비밀번호를 입력하세요")
    private String currentPassword;

    @Schema(description = "변경할 비밀번호", example = "1234")
    @NotEmpty(message = "변경할 비밀번호를 입력하세요")
    private String newPassword;
}
