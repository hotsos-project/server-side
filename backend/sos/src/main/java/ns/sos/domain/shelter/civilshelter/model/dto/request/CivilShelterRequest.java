package ns.sos.domain.shelter.civilshelter.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CivilShelterRequest {

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    @NotNull
    private double radius;

}