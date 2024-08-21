package ns.sos.domain.alarm.service;

import ns.sos.domain.alarm.model.AlarmInfo;
import ns.sos.domain.alarm.model.CommentAlarmInfo;
import ns.sos.domain.alarm.model.dto.request.AlarmGetRequest;
import ns.sos.domain.alarm.model.dto.response.AlarmResponses;
import ns.sos.domain.alarm.model.dto.response.MemberAlarmResponses;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;

public interface AlarmService {

    /**
     * 1. 알림 만들기 - type 별
     * 재난문자 알림 - 재난문자가 크롤링되면 해당 service 메소드 호출 필요
     * 댓글 알림 - 사용자가 댓글 작성 시 api 호출, 해당 메소드 호출됨
     * 핫이슈 알림 - 핫이슈로 선정(n개 이상 받기) 이벤트 발생 시 메소드 호출 필요
     * <p>
     * 알림을 만들고 해당 지역 구독중인 사용자에게 fcm 알림을 보내야 한다.
     * 2. 알림 조회하기 - member / member, type -> 날짜 오름차순
     * 전체 알림 조회, 지역별 알림 조회
     * 전체 알림 조회, 지역별 알림 조회 (comment, board 알림은 user-alarm에서만 조회 가능)
     * 알림 삭제하기 ? 있어야하나?
     * 알림 업데이트 ? 있어야하나??
     */

    void createAlarm(AlarmInfo alarmInfo, Integer keyId);

    int createAlarm(AlarmInfo alarmInfo, Sido sido, Gugun gugun, Integer keyId);

    int createAlarm(CommentAlarmInfo alarmInfo, Integer keyId);

    AlarmResponses getAlarms(AlarmGetRequest alarmGetRequest);

    void readAlarm(String alarmId, Integer memberId);

    MemberAlarmResponses getCommentAndHotIssueAlarms(Integer memberId, String timestamp, Integer limitPage);

}