package ns.sos.domain.member.exception;

import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

public class SmsCertificationNumberMismatchException extends BusinessException {
    public SmsCertificationNumberMismatchException(ErrorCode errorCode, String message) {
        super(errorCode, message);  // ErrorCode는 예시로 정의한 클래스
    }
}
