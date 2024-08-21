package ns.sos.domain.aed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AED {

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
    private String buildPlace;

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    private String tel;

    private String monTime;
    private String tueTime;
    private String wedTime;
    private String thuTime;
    private String friTime;
    private String holTime;

    public AED(final String sido,
               final String gugun,
               final String detailAddress,
               final String buildPlace,
               final double lat,
               final double lon,
               final String tel,
               final String monTime,
               final String tueTime,
               final String wedtime,
               final String thuTime,
               final String friTime,
               final String holTime) {
        this.sido = sido;
        this.gugun = gugun;
        this.detailAddress = detailAddress;
        this.buildPlace = buildPlace;
        this.lat = lat;
        this.lon = lon;
        this.tel = tel;
        this.monTime = monTime;
        this.tueTime = tueTime;
        this.wedTime = wedtime;
        this.thuTime = thuTime;
        this.friTime = friTime;
        this.holTime = holTime;
    }
}
