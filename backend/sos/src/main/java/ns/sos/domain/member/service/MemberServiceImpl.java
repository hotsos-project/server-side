package ns.sos.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.exception.ChangeLocationFailException;
import ns.sos.domain.member.exception.MemberNotFoundException;
import ns.sos.domain.member.exception.UpdateMemberInfoException;
import ns.sos.domain.member.exception.UpdatePasswordException;
import ns.sos.domain.member.model.MemberLocationInfo;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.model.dto.request.MemberChangeLocationRequest;
import ns.sos.domain.member.model.dto.request.MemberSearchRequest;
import ns.sos.domain.member.model.dto.request.MemberUpdateInfoRequest;
import ns.sos.domain.member.model.dto.request.MemberUpdatePasswordRequest;
import ns.sos.domain.member.model.dto.response.MemberInfoResponse;
import ns.sos.domain.member.model.dto.response.MemberSearchResponses;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.gugun.repository.GugunRepository;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.domain.region.sido.repository.SidoRepository;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import ns.sos.global.util.RedisUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final FirebaseService firebaseService;
    private final SidoRepository sidoRepository;
    private final GugunRepository gugunRepository;

    @Transactional(readOnly = true)
    @Override
    public MemberInfoResponse getMember(final Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "해당하는 회원이 없습니다."));
        return MemberInfoResponse.of(member.getId(), member.getLoginId(), member.getName(), member.getNickname(), member.getPhone(), member.getOauthType(), member.getRole());
    }

    @Override
    public void updateMemberPassword(MemberUpdatePasswordRequest memberUpdatePasswordRequest, Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UpdatePasswordException(ErrorCode.NOT_EXIST_MEMBER, "해당하는 회원이 없습니다."));
        if (!passwordEncoder.matches(memberUpdatePasswordRequest.getCurrentPassword(), member.getPassword())) {
            throw new UpdatePasswordException(ErrorCode.NOT_MATCH_PASSWORD, "회원의 비밀번호 정보와 일치하지 않습니다.");
        }
        member.updateMemberPassword(passwordEncoder.encode(memberUpdatePasswordRequest.getNewPassword()));
    }

    @Override
    public void updateMemberInfo(MemberUpdateInfoRequest memberUpdateInfoRequest, Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UpdateMemberInfoException(ErrorCode.NOT_EXIST_MEMBER, "해당하는 회원이 없습니다."));
        member.updateMemberInfo(memberUpdateInfoRequest.getNickname(), memberUpdateInfoRequest.getPhone());
    }

    @Override
    public void withdrawMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "해당 member 없음"));

        if (member.getStatus() == 'N') {
            throw new BusinessException(ErrorCode.ALREADY_WITHDRAWN_MEMBER, "이미 탈퇴 처리된 회원입니다.");
        }

        member.updateStatus('N');
        memberRepository.save(member);
    }

    @Override
    public MemberSearchResponses searchMember(MemberSearchRequest memberSearchRequest) {
        List<Member> members = memberRepository.findByNicknameOrLoginIdContaining(memberSearchRequest.getSearchKeyword());
        return MemberSearchResponses.of(members);
    }

    @Override
    public void changeLocation(MemberChangeLocationRequest memberChangeLocationRequest, final Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "해당 member 없음"));
        String sidoStr = Sido.mappingSido(memberChangeLocationRequest.getSido());
        Sido sido = sidoRepository.findByNameContaining(sidoStr);
        if (sido == null) {
            throw new ChangeLocationFailException(ErrorCode.NOT_EXIST_SIDO);
        }
        Gugun gugun = gugunRepository.findByNameContainingAndSido(memberChangeLocationRequest.getGugun(), sido);
        if (gugun == null) {
            throw new ChangeLocationFailException(ErrorCode.NOT_EXIST_GUGUN);
        }
        try {
            firebaseService.sendChangeUserLocationRequest(new MemberLocationInfo(member.getLoginId(), sido.getId().toString(), gugun.getId().toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSafe(Integer memberId) {
        // 키와 값을 정의
        String key = "safe-" + memberId;
        String value = "safe"; // 예시로 안전 상태를 나타내는 문자열

        // TTL 설정 (1일 = 86400초)
        long ttlInSeconds = 86400; // 1일 = 24시간 * 60분 * 60초

        // 데이터를 설정하고 TTL을 적용
        redisUtil.setData(key, value, ttlInSeconds);
    }
}
