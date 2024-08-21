package ns.sos.domain.alarm.repository;

import ns.sos.domain.alarm.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Integer>,AlarmCustomRepository {

}
