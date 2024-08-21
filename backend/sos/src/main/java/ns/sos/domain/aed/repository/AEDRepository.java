package ns.sos.domain.aed.repository;

import ns.sos.domain.aed.model.AED;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AEDRepository extends JpaRepository<AED, Integer> {

//    @Query(value = "SELECT * FROM aed l WHERE " +
//            "(6371 * acos(cos(radians(:lat)) * cos(radians(l.lat)) * cos(radians(l.lon) - radians(:lon)) + sin(radians(:lat)) * sin(radians(l.lat)))) <= :radius",
//            nativeQuery = true)
//    List<AED> findAllWithinRadius(@Param("lat") double lat, @Param("lon") double lon, @Param("radius") double radius);
    @Query(value = "SELECT * FROM aed WHERE lat BETWEEN :minLat AND :maxLat AND lon BETWEEN :minLon AND :maxLon",
            nativeQuery = true)
    List<AED> findAllWithinBoundingBox(@Param("minLat") double minLat,
                                       @Param("maxLat") double maxLat,
                                       @Param("minLon") double minLon,
                                       @Param("maxLon") double maxLon);
}
