package ns.sos.domain.shelter.etshelter.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ETShelterRequest {

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    @NotNull
    private double radius;

}