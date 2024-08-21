package ns.sos.domain.disaster.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class DisasterNotFoundException extends BusinessException {

    public DisasterNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DisasterNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
