package ns.sos.domain.boardbutton.service;

import ns.sos.domain.board.model.dto.response.BoardResponses;
import ns.sos.domain.boardbutton.model.dto.request.BoardButtonRequest;
import ns.sos.domain.boardbutton.model.dto.response.BoardButtonResponse;

public interface BoardButtonService {
    BoardButtonResponse clickFactButton(Integer memberId, BoardButtonRequest request);
    BoardButtonResponse clickReportButton(Integer memberId, BoardButtonRequest request);

    BoardResponses getTop3BoardsWithHighFactCount();
    String getButtonStatus(Integer memberId, Integer boardId);
}