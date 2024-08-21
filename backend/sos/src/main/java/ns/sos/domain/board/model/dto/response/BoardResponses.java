package ns.sos.domain.board.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "게시판 응답 목록 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponses {

    @Schema(description = "게시판 응답 리스트", example = "[,\"sido\":\"서울특별시\",\"gugun\":\"종로구\",\"address\":\"서울특별시 종로구 세종대로 110\",\"count\":100}]")
    private final List<BoardResponse> boardResponses;

    public static BoardResponses from(final List<BoardResponse> boardResponses) {
        return new BoardResponses(boardResponses);
    }
}
