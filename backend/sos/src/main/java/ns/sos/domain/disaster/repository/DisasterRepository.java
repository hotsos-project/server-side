package ns.sos.domain.disaster.repository;

import io.lettuce.core.dynamic.annotation.Param;
import ns.sos.domain.disaster.model.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DisasterRepository extends JpaRepository<Disaster, Integer> {

    @Query("SELECT MAX(d.serialNumber) FROM Disaster d")
    Integer findMaxSerialNumber();

    @Query(value = "SELECT * FROM disaster d WHERE (:lastDisasterId IS NULL OR d.id < :lastDisasterId) ORDER BY d.id DESC LIMIT :limitPage", nativeQuery = true)
    List<Disaster> findDisastersWithPagination(@Param("lastDisasterId") Integer lastDisasterId, @Param("limitPage") int limitPage);

    @Query(value = "SELECT * FROM disaster d WHERE d.locationName LIKE %:location% AND (:lastDisasterId IS NULL OR d.id < :lastDisasterId) ORDER BY d.id DESC LIMIT :limitPage", nativeQuery = true)
    List<Disaster> findByLocationNameContainingPagination(@Param("location") String location, @Param("lastDisasterId") Integer lastDisasterId, @Param("limitPage") int limitPage);
}
