package ns.sos.domain.board.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "게시판 request dto", description = "게시판 수정시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class BoardUpdateRequest {

    @Schema(description = "제목", example = "modify title1")
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;

    @Schema(description = "내용", example = "modify content1")
    @NotEmpty(message = "내용을 입력하세요")
    private String content;
}