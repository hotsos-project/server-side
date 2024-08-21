package ns.sos.domain.shelter.eoshelter.repository;

import ns.sos.domain.shelter.eoshelter.model.EOShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EOShelterRepository extends JpaRepository<EOShelter, Integer> {

    @Query(value = "SELECT * FROM eo_shelter WHERE lat BETWEEN :minLat AND :maxLat AND lon BETWEEN :minLon AND :maxLon",
            nativeQuery = true)
    List<EOShelter> findAllWithinBoundingBox(@Param("minLat") double minLat,
                                                @Param("maxLat") double maxLat,
                                                @Param("minLon") double minLon,
                                                @Param("maxLon") double maxLon);
}