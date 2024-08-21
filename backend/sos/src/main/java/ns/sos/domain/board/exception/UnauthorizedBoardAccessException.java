package ns.sos.domain.board.exception;

public class UnauthorizedBoardAccessException extends RuntimeException {
    public UnauthorizedBoardAccessException(String message) {
        super(message);
    }
}