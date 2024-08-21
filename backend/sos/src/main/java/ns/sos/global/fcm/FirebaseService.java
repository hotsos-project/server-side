package ns.sos.global.fcm;


import ns.sos.domain.alarm.model.AlarmInfo;
import ns.sos.domain.alarm.model.CommentAlarmInfo;
import ns.sos.domain.alarm.model.dto.request.AlarmReadRequest;
import ns.sos.domain.alarm.model.dto.response.MemberAlarmResponses;
import ns.sos.domain.disaster.model.FirebaseGetDisasterInfo;
import ns.sos.domain.favoriteregion.model.dto.request.FavoriteRegionFirebaseInfo;
import ns.sos.domain.follow.model.dto.FollowFirebaseInfo;
import ns.sos.domain.member.model.MemberFirebaseInfo;
import ns.sos.domain.member.model.MemberLocationInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface FirebaseService {

    int sendRegionNotification(AlarmInfo alarmInfo, String alarmId) throws IOException;

    void sendAlarmNotification(CommentAlarmInfo alarmInfo, String alarmId) throws IOException;

    void sendFollowRequest(FollowFirebaseInfo followFirebaseInfo) throws IOException;

    void sendRegisterRequest(MemberFirebaseInfo memberFirebaseInfo) throws IOException;

    void sendRegionRequest(FavoriteRegionFirebaseInfo favoriteRegionFirebaseInfo) throws IOException;

    void sendReadAlarmRequest(AlarmReadRequest alarmReadRequest) throws IOException;

    MemberAlarmResponses getMemberAlarmResponses(String memberLoginId, String timestamp, Integer limitPage) throws IOException;

    List<FirebaseGetDisasterInfo> getDisasterMessagesOfMemberAndFollowers(String memberLoginId, String timestamp, Integer limitPage) throws IOException;

    void sendChangeUserLocationRequest(MemberLocationInfo memberLocationInfo) throws IOException;
}
