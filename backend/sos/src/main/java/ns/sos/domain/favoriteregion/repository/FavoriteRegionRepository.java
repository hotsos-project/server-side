package ns.sos.domain.favoriteregion.repository;

import ns.sos.domain.favoriteregion.model.FavoriteRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRegionRepository extends JpaRepository<FavoriteRegion, Integer> {
    List<FavoriteRegion> findByMemberId(Integer memberId);
    boolean existsByMemberId(Integer memberId);
}
