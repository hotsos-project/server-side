package ns.sos.domain.region.gugun.repository;

import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GugunRepository extends JpaRepository<Gugun, Integer> {

    Gugun findByNameContainingAndSido(String name, Sido sido);
    List<Gugun> findAllBySidoId(Integer sidoId);
}
