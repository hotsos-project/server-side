package ns.sos.domain.boardbutton.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "게시판 버튼 request dto", description = "게시판 버튼 클릭시 필요한 정보 ")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardButtonRequest {

    @NotNull(message = "게시글 ID를 입력하세요.")
    private Integer boardId;

}