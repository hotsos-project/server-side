package ns.sos.domain.shelter.civilshelter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "civil_shelter")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CivilShelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String sido;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String gugun;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String detailAddress;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String name;

    @NotNull
    private int capacity;

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    public CivilShelter(final String sido,
                        final String gugun,
                        final String detailAddress,
                        final String name,
                        final int capacity,
                        final double lat,
                        final double lon) {
        this.sido = sido;
        this.gugun = gugun;
        this.detailAddress = detailAddress;
        this.name = name;
        this.capacity = capacity;
        this.lat = lat;
        this.lon = lon;
    }
}
