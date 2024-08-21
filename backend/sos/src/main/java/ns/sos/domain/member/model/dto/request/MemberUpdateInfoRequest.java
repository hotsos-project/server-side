package ns.sos.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "회원정보 수정 request dto", description = "회원정보 수정시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberUpdateInfoRequest {

    @Schema(description = "닉네임", example = "변경닉네임1")
    @NotEmpty(message = "닉네임을 입력하세요")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1111-2222")
    @NotEmpty(message = "전화번호를 입력하세요")
    private String phone;
}