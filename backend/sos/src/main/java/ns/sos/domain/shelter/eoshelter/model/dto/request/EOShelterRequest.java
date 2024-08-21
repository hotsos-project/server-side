package ns.sos.domain.shelter.eoshelter.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EOShelterRequest {

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    @NotNull
    private double radius;

}