package ns.sos.domain.region.gugun.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.region.sido.model.Sido;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "gugun")
@Entity
public class Gugun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id", nullable = false)
    private Sido sido;

    private Gugun(final Sido sido, final String name) {
        this.sido = sido;
        this.name = name;
    }

    public static Gugun of(final Sido sido, final String name) {
        return new Gugun(sido, name);
    }
}
