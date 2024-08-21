package ns.sos.domain.favoriteregion.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;

@Entity
@Table(name = "favorite_region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id", nullable = false)
    private Sido sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gugun_id", nullable = false)
    private Gugun gugun;

    private FavoriteRegion(final Member member, final Sido sido, final Gugun gugun) {
        this.member = member;
        this.sido = sido;
        this.gugun = gugun;
    }

    public static FavoriteRegion of(final Member member, final Sido sido, final Gugun gugun) {
        return new FavoriteRegion(member, sido, gugun);
    }

    public void updateRegion(Sido sido, Gugun gugun) {
        this.sido = sido;
        this.gugun = gugun;
    }
}
