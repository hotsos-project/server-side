package ns.sos.domain.shelter.etshelter.repository;

import ns.sos.domain.shelter.etshelter.model.ETShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ETShelterRepository extends JpaRepository<ETShelter, Integer> {

    @Query(value = "SELECT * FROM et_shelter WHERE lat BETWEEN :minLat AND :maxLat AND lon BETWEEN :minLon AND :maxLon",
            nativeQuery = true)
    List<ETShelter> findAllWithinBoundingBox(@Param("minLat") double minLat,
                                             @Param("maxLat") double maxLat,
                                             @Param("minLon") double minLon,
                                             @Param("maxLon") double maxLon);
}