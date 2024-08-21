package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class OauthLoginFailException extends BusinessException {

    public OauthLoginFailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OauthLoginFailException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
