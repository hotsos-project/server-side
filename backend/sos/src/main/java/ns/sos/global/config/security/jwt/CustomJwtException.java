package ns.sos.global.config.security.jwt;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class CustomJwtException extends BusinessException {
    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CustomJwtException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
