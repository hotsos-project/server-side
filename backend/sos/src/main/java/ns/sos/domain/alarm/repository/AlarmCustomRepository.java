package ns.sos.domain.alarm.repository;

import ns.sos.domain.alarm.model.dto.request.AlarmGetRequest;
import ns.sos.domain.alarm.model.dto.response.AlarmResponse;

import java.util.List;

public interface AlarmCustomRepository {
    List<AlarmResponse> getAlarmResponses(AlarmGetRequest alarmGetRequest);
}
