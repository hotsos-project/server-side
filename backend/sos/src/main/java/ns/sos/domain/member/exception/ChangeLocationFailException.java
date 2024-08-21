package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class ChangeLocationFailException extends BusinessException {

    public ChangeLocationFailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChangeLocationFailException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
