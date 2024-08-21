package ns.sos.domain.disaster.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.alarm.model.AlarmInfo;
import ns.sos.domain.alarm.model.AlarmKeyword;
import ns.sos.domain.alarm.model.AlarmType;
import ns.sos.domain.alarm.service.AlarmService;
import ns.sos.domain.disaster.exception.DisasterNotFoundException;
import ns.sos.domain.disaster.model.Disaster;
import ns.sos.domain.disaster.model.FirebaseGetDisasterInfo;
import ns.sos.domain.disaster.model.dto.response.DisasterResponse;
import ns.sos.domain.disaster.model.dto.response.DisasterResponses;
import ns.sos.domain.disaster.model.dto.response.MemberDisasterResponse;
import ns.sos.domain.disaster.model.dto.response.MemberDisasterResponses;
import ns.sos.domain.disaster.repository.DisasterRepository;
import ns.sos.domain.follow.repository.FollowRepository;
import ns.sos.domain.member.exception.MemberNotFoundException;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.gugun.repository.GugunRepository;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.domain.region.sido.repository.SidoRepository;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import ns.sos.infra.disaster.CrawInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DisasterServiceImpl implements DisasterService {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final DisasterRepository disasterRepository;
    private final SidoRepository sidoRepository;
    private final GugunRepository gugunRepository;
    private final FirebaseService firebaseService;
    private final AlarmService alarmService;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Override
    public DisasterResponses findAll(final Integer lastDisasterId, final Integer limitPage) {
        List<Disaster> disasters = disasterRepository.findDisastersWithPagination(lastDisasterId, limitPage);
        List<DisasterResponse> list = disasters.stream()
                .map(DisasterResponse::from)
                .toList();

        return DisasterResponses.from(list);
    }

    @Override
    public DisasterResponses findByLocation(final String location, final Integer lastDisasterId, final Integer limitPage) {
        List<Disaster> disasters = disasterRepository.findByLocationNameContainingPagination(location, lastDisasterId, limitPage);
        List<DisasterResponse> list = disasters.stream()
                .map(DisasterResponse::from)
                .toList();

        return DisasterResponses.from(list);
    }

    @Override
    public DisasterResponse findById(Integer id) {
        Disaster disaster = disasterRepository.findById(id).orElseThrow(() -> new DisasterNotFoundException(ErrorCode.NOT_EXIST_DISASTER, "해당하는 재난문자가 존재하지 않습니다."));
        return DisasterResponse.from(disaster);
    }

    @Override
    public MemberDisasterResponses getDisasterMessagesOfMemberAndFollowers(Integer memberId, String timestamp, Integer limitPage) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER));
        try {
            List<FirebaseGetDisasterInfo> firebaseResponses = firebaseService.getDisasterMessagesOfMemberAndFollowers(member.getLoginId(), timestamp, limitPage);
            List<MemberDisasterResponse> memberDisasterResponses = firebaseResponses.stream()
                    .filter(f -> {
                        if (f.isMine()) {
                            return disasterRepository.existsById(f.getKeyId());
                        }
                        return disasterRepository.existsById(f.getKeyId()) && memberRepository.findByLoginIdAndStatus(f.getFollowLoginId(), 'Y').isPresent();
                    })
                    .map(f -> {
                        Disaster disaster = disasterRepository.findById(f.getKeyId()).get();
                        DisasterResponse disasterResponse = DisasterResponse.from(disaster);
                        String nickname = "";
                        if (!f.isMine()){
                            Member followMember = memberRepository.findByLoginIdAndStatus(f.getFollowLoginId(), 'Y').get();
                            nickname = followRepository.findByMemberIdAndFollowMemberId(memberId, followMember.getId()).get().getNickName();
                        }

                        
                        return MemberDisasterResponse.of(disasterResponse, f, nickname);
                    })
                    .toList();
            return MemberDisasterResponses.from(memberDisasterResponses);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Disaster> saveAll(final List<CrawInfo> crawDisasters) {
        List<Disaster> disasters = disasterRepository.saveAll(getCrawDisasters(crawDisasters));

        // fcm 알림 요청
        for (Disaster disaster : disasters) {
            //title과 content를 만들어야 한다.
            AlarmKeyword alarmKeyword = AlarmKeyword.getKeyword(disaster);
            List<String> token = new ArrayList<>();
            String keyword = disaster.getClassification();
            String title = title = "[재난문자] " + disaster.getSido().getName() + " " + keyword;
            String content = disaster.getMsg();
            if (alarmKeyword != null) {
                keyword = alarmKeyword.getKeyword();
                token = AlarmKeyword.getKeyword(keyword).getTokens(content);
                content = alarmKeyword.getContent(token);
            }

            AlarmInfo alarmInfo = new AlarmInfo(
                    AlarmType.DISASTER.getType(),
                    disaster.getSido().getId().toString(),
                    disaster.getGugun() == null ? "" : disaster.getGugun().getId().toString(),
                    title,
                    content,
                    keyword,
                    token,
                    String.valueOf(disaster.getId()));

//            log.info("title: ")
            int alarmId = alarmService.createAlarm(alarmInfo, disaster.getSido(), disaster.getGugun(), disaster.getId());

            try {
                firebaseService.sendRegionNotification(alarmInfo, String.valueOf(alarmId));
            } catch (IOException e) {
                //todo: 예외처리 - 모아서 다시 보내기 등
                throw new RuntimeException(e);
            }
        }

        return disasters;
    }

    private List<Disaster> getCrawDisasters(final List<CrawInfo> crawDisasters) {
        return crawDisasters.reversed().stream()
                .map(disaster -> {
                    String[] title = disaster.getMessage().split(" "); // 날짜, 시간
                    String[] regions = disaster.getLocation().split(" ");

                    Sido sido = sidoRepository.findByNameContaining(regions[0]);

                    Gugun gugun = null;

                    if (regions.length > 1) {
                        gugun = gugunRepository.findByNameContainingAndSido(regions[1], sido);
                    }

                    try {
                        return new Disaster(Integer.parseInt(disaster.getDisasterNo()),
                                formatter.parse(title[0] + " " + title[1]),
                                disaster.getLevel(),
                                disaster.getClassification(),
                                disaster.getDetailMessage(),
                                disaster.getLocation(),
                                sido,
                                gugun);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }
}
