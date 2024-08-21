package ns.sos.domain.aed.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AEDRequest {

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    @NotNull
    private double radius;
}