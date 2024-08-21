package ns.sos.domain.reply.exception;

public class UnauthorizedReplyAccessException extends RuntimeException {
    public UnauthorizedReplyAccessException(String message) {
        super(message);
    }
}