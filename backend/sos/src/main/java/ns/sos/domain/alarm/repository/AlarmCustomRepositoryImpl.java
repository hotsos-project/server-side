package ns.sos.domain.alarm.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.alarm.model.AlarmType;
import ns.sos.domain.alarm.model.dto.request.AlarmGetRequest;
import ns.sos.domain.alarm.model.dto.response.AlarmResponse;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static ns.sos.domain.alarm.model.QAlarm.alarm;

@Repository
@RequiredArgsConstructor
public class AlarmCustomRepositoryImpl implements AlarmCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AlarmResponse> getAlarmResponses(AlarmGetRequest alarmGetRequest) {
        Integer lastAlarmId = alarmGetRequest.getLastAlarmId();
        Integer limitPage = alarmGetRequest.getLimitPage();

        return queryFactory
                .select(Projections.constructor(AlarmResponse.class, alarm))
                .from(alarm)
                .where(
                        typeEquals(alarmGetRequest.getType()),
                        sidoEquals(alarmGetRequest.getSido()),
                        gugunEquals(alarmGetRequest.getGugun()),
                        lastIdLessThan(lastAlarmId) // 마지막 ID보다 작은 값들만 조회
                )
                .orderBy(alarm.id.desc()) // ID를 기준으로 내림차순 정렬
                .limit(limitPage) // 결과를 limitPage 개수만큼만 가져옴
                .fetch();
    }

    private BooleanExpression lastIdLessThan(Integer lastAlarmId) {
        return lastAlarmId != null ? alarm.id.lt(lastAlarmId) : null;
    }
    private BooleanExpression typeEquals(String alarmType) {
        return alarm.type.eq(AlarmType.valueOf(alarmType));
    }

    private BooleanExpression sidoEquals(String sido) {
        if(!StringUtils.hasText(sido)){
            return null;
        }
        return alarm.sido.name.eq(sido);
    }

    private BooleanExpression gugunEquals(String gugun) {
        if(!StringUtils.hasText(gugun)){
            return null;
        }
        return alarm.gugun.name.eq(gugun);
    }
}
