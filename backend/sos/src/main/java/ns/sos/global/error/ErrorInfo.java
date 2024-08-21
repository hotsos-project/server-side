package ns.sos.global.error;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ErrorInfo {

    private final String code;
    private final String trackingId;
    private final String detailMessage;

    public ErrorInfo(ErrorCode code, String detailMessage) {
        this.code = code.getCode();
        this.trackingId = UUID.randomUUID().toString();
        this.detailMessage = detailMessage;
    }

    public ErrorInfo(ErrorCode code) {
        this.code = code.getCode();
        this.trackingId = UUID.randomUUID().toString();
        this.detailMessage = "";
    }
}