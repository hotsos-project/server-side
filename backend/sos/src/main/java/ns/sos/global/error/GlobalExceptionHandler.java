package ns.sos.global.error;

import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.board.exception.BoardNotFoundException;
import ns.sos.domain.board.exception.UnauthorizedBoardAccessException;
import ns.sos.global.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Response<ErrorInfo>> businessExceptionHandler(BusinessException e){
        log.error("Businessexception - code: {} / message: {} / class: {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage(), e.getClass());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(Response.ERROR(e.getErrorCode(), e.getMessage()));
    }

    //todo : 그 외 다른 exception handler 추가 필요
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<Response<ErrorInfo>> handleBoardNotFoundException(BoardNotFoundException ex) {
        log.error("BoardNotFoundException - message: {} / class: {}", ex.getMessage(), ex.getClass());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.ERROR(ErrorCode.NOT_FOUND_DATA, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedBoardAccessException.class)
    public ResponseEntity<Response<ErrorInfo>> handleUnauthorizedBoardAccessException(UnauthorizedBoardAccessException ex) {
        log.error("UnauthorizedBoardAccessException - message: {} / class: {}", ex.getMessage(), ex.getClass());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.ERROR(ErrorCode.UNAUTHORIZED, ex.getMessage()));
    }

    // 기타 예외 핸들러를 추가할 수 있습니다.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<ErrorInfo>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException - message: {} / class: {}", ex.getMessage(), ex.getClass());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.ERROR(ErrorCode.BAD_REQUEST, "Required parameter is missing: " + ex.getParameterName()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Response<ErrorInfo>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.error("AuthorizationDeniedException - message: {} / class: {}", ex.getMessage(), ex.getClass());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.ERROR(ErrorCode.UNAUTHORIZED, "관리자만 접근할 수 있습니다. " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Response<ErrorInfo>> handleException(Exception ex){
        log.error("Exception - message: {} / class: {}", ex.getMessage(), ex.getClass());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.ERROR(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

}