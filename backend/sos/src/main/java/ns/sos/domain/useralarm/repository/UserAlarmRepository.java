package ns.sos.domain.useralarm.repository;

import ns.sos.domain.useralarm.model.UserAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlarmRepository extends JpaRepository<UserAlarm, Integer> {
}
