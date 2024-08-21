package ns.sos.domain.boardbutton.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.boardbutton.model.BoardButton;

@Schema(name = "누른 버튼 dto", description = "누른 버튼의 정보")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardButtonResponse {

    @Schema(description = "생성된 버튼의 pk값", example = "1")
    private Integer id;

    @Schema(description = "게시판 id", example = "2")
    private Integer boardId;

    @Schema(description = "멤버 id", example = "1")
    private Integer memberId;

    @Schema(description = "버튼 타입", example = "fact")
    private String type;

    public static BoardButtonResponse from(BoardButton boardButton) {
        return new BoardButtonResponse(
                boardButton.getId(),
                boardButton.getBoard().getId(),
                boardButton.getMember().getId(),
                boardButton.getType()
        );
    }
}