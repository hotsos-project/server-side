package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class LoginFailException extends BusinessException {

    public LoginFailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LoginFailException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
