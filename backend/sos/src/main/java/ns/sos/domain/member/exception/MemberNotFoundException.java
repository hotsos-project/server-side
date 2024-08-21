package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}