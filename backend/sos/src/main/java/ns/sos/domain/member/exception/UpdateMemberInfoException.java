package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class UpdateMemberInfoException extends BusinessException {
    public UpdateMemberInfoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UpdateMemberInfoException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
