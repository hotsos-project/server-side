package ns.sos.domain.shelter.civilshelter.repository;

import ns.sos.domain.shelter.civilshelter.model.CivilShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CivilShelterRepository extends JpaRepository<CivilShelter, Integer> {

    @Query(value = "SELECT * FROM civil_shelter WHERE lat BETWEEN :minLat AND :maxLat AND lon BETWEEN :minLon AND :maxLon",
            nativeQuery = true)
    List<CivilShelter> findAllWithinBoundingBox(@Param("minLat") double minLat,
                                       @Param("maxLat") double maxLat,
                                       @Param("minLon") double minLon,
                                       @Param("maxLon") double maxLon);
}