package ns.sos.domain.disaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;

import java.util.Date;

@Entity
@Table(name = "disaster")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Disaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String locationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private Sido sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gugun_id")
    private Gugun gugun;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String msg;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String classification;

    @NotEmpty(message = "빈값이 들어올 수 없습니다.")
    private String level;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime;

    @NotNull
    private int serialNumber;

    public Disaster(final int serialNumber,
                    final Date sendTime,
                    final String level,
                    final String classification,
                    final String msg,
                    final String locationName,
                    final Sido sido,
                    final Gugun gugun) {
        this.serialNumber = serialNumber;
        this.sendTime = sendTime;
        this.level = level;
        this.classification = classification;
        this.msg = msg;
        this.locationName = locationName;
        this.sido = sido;
        this.gugun = gugun;
    }
}
