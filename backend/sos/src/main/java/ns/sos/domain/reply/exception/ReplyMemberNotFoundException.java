package ns.sos.domain.reply.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class ReplyMemberNotFoundException extends BusinessException {
    public ReplyMemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReplyMemberNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
