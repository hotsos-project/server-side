package ns.sos.domain.region.sido.repository;

import ns.sos.domain.region.sido.model.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoRepository extends JpaRepository<Sido, Integer> {

    Sido findByNameContaining(String name);
}
