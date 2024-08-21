package ns.sos.domain.reply.exception;

public class ReplyNotFoundException extends RuntimeException {
    public ReplyNotFoundException(String message) {
        super(message);
    }
}