package ns.sos.global.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.alarm.model.AlarmInfo;
import ns.sos.domain.alarm.model.CommentAlarmInfo;
import ns.sos.domain.alarm.model.dto.request.AlarmReadRequest;
import ns.sos.domain.alarm.model.dto.response.MemberAlarmResponse;
import ns.sos.domain.alarm.model.dto.response.MemberAlarmResponses;
import ns.sos.domain.disaster.model.FirebaseGetDisasterInfo;
import ns.sos.domain.favoriteregion.model.dto.request.FavoriteRegionFirebaseInfo;
import ns.sos.domain.follow.model.dto.FollowFirebaseInfo;
import ns.sos.domain.member.model.MemberFirebaseInfo;
import ns.sos.domain.member.model.MemberLocationInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Slf4j
@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Value("${fcm.url}")
    private String fcmUrl;

    private final WebClient webClient;

    public FirebaseServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(fcmUrl).build();
    }

    @Override
    public int sendRegionNotification(AlarmInfo alarmInfo, String alarmId) throws IOException {
        Map<String, String> body = new HashMap<>();

        body.put("sido", alarmInfo.getSido());
        body.put("gugun", alarmInfo.getGugun());
        body.put("title", alarmInfo.getTitle());
        body.put("content", alarmInfo.getContent());
        body.put("alarmType", alarmInfo.getAlarmType());
        body.put("keyword", alarmInfo.getKeyword());
        body.put("textList", alarmInfo.getTextList().toString());
        body.put("keyId", alarmInfo.getKeyId());
        body.put("alarmId", alarmId);

        Mono<String> responseMono = sendRequestToFirebase(body, "sendRegionNotificationWithFollows");
        responseMono.subscribe(System.out::println);

        return 1;
    }

    @Override
    public void sendAlarmNotification(CommentAlarmInfo alarmInfo, String alarmId) throws IOException {
        Map<String, String> body = new HashMap<>();

        body.put("title", alarmInfo.getTitle());
        body.put("content", alarmInfo.getContent());
        body.put("alarmType", alarmInfo.getAlarmType());
        body.put("keyword", alarmInfo.getKeyword());
        body.put("textList", alarmInfo.getTextList().toString());
        body.put("userId", alarmInfo.getLoginId());
        body.put("keyId", alarmInfo.getKeyId());
        body.put("alarmId", alarmId);
        Mono<String> responseMono = sendRequestToFirebase(body, "sendBoardOwner");
        responseMono.subscribe(System.out::println);
    }

    @Override
    public void sendFollowRequest(FollowFirebaseInfo followFirebaseInfo) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("memberLoginId", followFirebaseInfo.getMemberLoginId());
        body.put("followLoginId", followFirebaseInfo.getFollowerLoginId());
        body.put("isFollow", followFirebaseInfo.getIsFollow());
        Mono<String> responseMono = sendRequestToFirebase(body, "handleFollow");
        responseMono.subscribe(System.out::println);
    }

    @Override
    public void sendRegisterRequest(MemberFirebaseInfo memberFirebaseInfo) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("userId", memberFirebaseInfo.getMemberLoginId());
        body.put("fcmToken", memberFirebaseInfo.getFcmToken());
        body.put("sido", memberFirebaseInfo.getSido());
        body.put("gugun", memberFirebaseInfo.getGugun());
        Mono<String> responseMono = sendRequestToFirebase(body, "join");
        responseMono.subscribe(System.out::println);
    }

    @Override
    public void sendRegionRequest(FavoriteRegionFirebaseInfo favoriteRegionFirebaseInfo) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("favoriteSido", favoriteRegionFirebaseInfo.getFavoriteSido());
        body.put("favoriteGugun", favoriteRegionFirebaseInfo.getFavoriteGugun());
        body.put("userLoginId", favoriteRegionFirebaseInfo.getUserLoginId());
        body.put("isAdd", favoriteRegionFirebaseInfo.getIsAdd());
        Mono<String> responseMono = sendRequestToFirebase(body, "updateFavoriteRegion");
        responseMono.subscribe(System.out::println);
    }

    @Override
    public void sendReadAlarmRequest(AlarmReadRequest alarmReadRequest) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("userAlarmId", alarmReadRequest.getAlarmId());
        body.put("userLoginId", alarmReadRequest.getLoginId());
        Mono<String> responseMono = sendRequestToFirebase(body, "readAlarm");
        responseMono.subscribe(System.out::println);
    }

    @Override
    public MemberAlarmResponses getMemberAlarmResponses(String memberLoginId, String timestamp, Integer limitPage) throws IOException {
        timestamp = (timestamp == null) ? "" : String.valueOf(convertToSeconds(timestamp));
        Map<String, String> body = new HashMap<>();
        body.put("userLoginId", memberLoginId);
        body.put("seconds", timestamp);
        body.put("limitPage", limitPage.toString());

        String response = sendRequestToFirebase(body, "getAlarms").block();

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(response);
            List<MemberAlarmResponse> alarms = new ArrayList<>();

            for (JsonNode alarmNode : jsonNode) {
                MemberAlarmResponse memberAlarmResponse = MemberAlarmResponse.fromJson(alarmNode);
                alarms.add(memberAlarmResponse);
            }

            return MemberAlarmResponses.from(alarms);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 에러", e);
        }
    }

    @Override
    public List<FirebaseGetDisasterInfo> getDisasterMessagesOfMemberAndFollowers(String memberLoginId, String timestamp, Integer limitPage) throws IOException {
        timestamp = (timestamp == null) ? "" : String.valueOf(convertToSeconds(timestamp));

        Map<String, String> body = new HashMap<>();
        body.put("userLoginId", memberLoginId);
        body.put("seconds", timestamp);
        body.put("limitPage", limitPage.toString());
        String response = sendRequestToFirebase(body, "getDisasterMessagesOfMeAndFollowersPagination").block();

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(response);
            List<FirebaseGetDisasterInfo> firebaseGetDisasterInfos = new ArrayList<>();

            for (JsonNode disasterNode : jsonNode) {
                FirebaseGetDisasterInfo firebaseGetDisasterInfo = FirebaseGetDisasterInfo.fromJson(disasterNode);
                firebaseGetDisasterInfos.add(firebaseGetDisasterInfo);
            }

            return firebaseGetDisasterInfos;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 에러", e);
        }
    }

    @Override
    public void sendChangeUserLocationRequest(MemberLocationInfo memberLocationInfo) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("userLoginId", memberLocationInfo.getMemberLoginId());
        body.put("sido", memberLocationInfo.getSido());
        body.put("gugun", memberLocationInfo.getGugun());
        Mono<String> responseMono = sendRequestToFirebase(body, "changeLocation");
        responseMono.subscribe(System.out::println);
    }

    public long convertToSeconds(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    private Mono<String> sendRequestToFirebase(Map<String, String> body, String uri) throws IOException {
        return webClient.post()
                .uri("/" + uri)
                .bodyValue(body)
                .retrieve()
//                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
//                    String errorMsg = "Failed to send FCM message with status code: " + clientResponse.statusCode();
//                    System.err.println(errorMsg);
//                    return Mono.error(new RuntimeException(errorMsg));
//                })
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("Successfully sent FCM message: " + response));
//                .doOnError(error -> System.err.println(
//                        "Error 404 : 해당 하는 사용자가 없을 경우 occurred while sending FCM message: " + error.getMessage()));

    }
}

