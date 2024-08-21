package ns.sos.domain.board.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class BoardMemberNotFoundException extends BusinessException {
    public BoardMemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BoardMemberNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
