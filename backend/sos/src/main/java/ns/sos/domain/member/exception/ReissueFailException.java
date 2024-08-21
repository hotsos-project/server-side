package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class ReissueFailException extends BusinessException {

    public ReissueFailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReissueFailException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
