package ns.sos.domain.comment.exception;

public class UnauthorizedCommentAccessException extends RuntimeException {
    public UnauthorizedCommentAccessException(String message) {
        super(message);
    }
}