package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "로그인 request dto", description = "로그인 시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberLoginRequest {

    @Schema(description = "아이디", example = "asdf")
    @NotEmpty(message = "아이디를 입력하세요")
    private String loginId;

    @Schema(description = "비밀번호", example = "asdf")
    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String password;
}
