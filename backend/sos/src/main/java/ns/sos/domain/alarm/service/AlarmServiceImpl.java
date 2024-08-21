package ns.sos.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.alarm.model.*;
import ns.sos.domain.alarm.model.dto.request.AlarmGetRequest;
import ns.sos.domain.alarm.model.dto.request.AlarmReadRequest;
import ns.sos.domain.alarm.model.dto.response.AlarmResponses;
import ns.sos.domain.alarm.model.dto.response.MemberAlarmResponses;
import ns.sos.domain.alarm.repository.AlarmRepository;
import ns.sos.domain.member.exception.MemberNotFoundException;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final FirebaseService firebaseService;

    /**
     * 알람을 만듭니다.
     *
     * @param alarmInfo
     */
    @Override
    public void createAlarm(AlarmInfo alarmInfo, Integer keyId) {
        Alarm alarm = Alarm.from(alarmInfo, keyId);
        alarmRepository.save(alarm);
    }

    @Override
    public int createAlarm(AlarmInfo alarmInfo, Sido sido, Gugun gugun, Integer keyId) {
        System.out.println(alarmInfo.getAlarmType());
        System.out.println(AlarmType.getAlarmType(alarmInfo.getAlarmType()));
        Alarm alarm = Alarm.of(alarmInfo, keyId, sido, gugun);
        System.out.println(alarm.getType());
        Alarm savedAlarm = alarmRepository.save(alarm);
        return savedAlarm.getId();
    }

    @Override
    public int createAlarm(CommentAlarmInfo alarmInfo, Integer keyId) {
        Alarm alarm = Alarm.of(alarmInfo, keyId);
        Alarm savedAlarm = alarmRepository.save(alarm);
        return savedAlarm.getId();
    }

    //todo : 추후 pagination 구현 필요

    /**
     * 타입별, 시도, 구군별 알람을 조회합니다.
     *
     * @param alarmGetRequest
     * @return
     */
    @Override
    public AlarmResponses getAlarms(AlarmGetRequest alarmGetRequest) {
        return AlarmResponses.from(alarmRepository.getAlarmResponses(alarmGetRequest));
    }

    @Override
    public void readAlarm(final String alarmId, final Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER));
        try {
            firebaseService.sendReadAlarmRequest(new AlarmReadRequest(alarmId, member.getLoginId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MemberAlarmResponses getCommentAndHotIssueAlarms(Integer memberId, String timestamp, Integer limitPage) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER));
        try {
            return firebaseService.getMemberAlarmResponses(member.getLoginId(), timestamp, limitPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
