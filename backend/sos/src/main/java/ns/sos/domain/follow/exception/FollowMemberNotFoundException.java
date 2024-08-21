package ns.sos.domain.follow.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class FollowMemberNotFoundException extends BusinessException {
    public FollowMemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FollowMemberNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
