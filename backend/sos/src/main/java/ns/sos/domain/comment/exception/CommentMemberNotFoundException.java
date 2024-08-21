package ns.sos.domain.comment.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class CommentMemberNotFoundException extends BusinessException {
    public CommentMemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommentMemberNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
