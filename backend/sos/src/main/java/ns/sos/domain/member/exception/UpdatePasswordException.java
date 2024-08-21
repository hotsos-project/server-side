package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class UpdatePasswordException extends BusinessException {
    public UpdatePasswordException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UpdatePasswordException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
