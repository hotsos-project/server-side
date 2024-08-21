package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class RegisterFailException extends BusinessException {

    public RegisterFailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RegisterFailException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
