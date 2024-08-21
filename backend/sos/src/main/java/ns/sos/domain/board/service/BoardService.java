package ns.sos.domain.board.service;

import ns.sos.domain.board.model.dto.request.BoardCreateRequest;
import ns.sos.domain.board.model.dto.request.BoardUpdateRequest;
import ns.sos.domain.board.model.dto.response.BoardResponse;
import ns.sos.domain.board.model.dto.response.BoardResponses;
import ns.sos.domain.region.sido.model.Sido;

public interface BoardService {

    BoardResponse getBoardById(Integer id);

    BoardResponses getLatestBoards(Integer lastBoardId, Integer limitPage);

    BoardResponses searchBoards(String keyword, Integer lastBoardId, Integer limitPage);

    BoardResponses searchBoardsByMemberId(Integer memberId, Integer lastBoardId, Integer limitPageCount);

    BoardResponse createBoard(BoardCreateRequest request, Integer memberId);

    BoardResponse updateBoard(Integer id, BoardUpdateRequest request, Integer memberId);

    void deleteBoard(Integer id, Integer memberId);

    BoardResponses searchBoardsBySido(String sido, Integer lastBoardId, Integer limitPageCount);
}