package ns.sos.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.exception.LoginFailException;
import ns.sos.domain.member.exception.OauthLoginFailException;
import ns.sos.domain.member.exception.RegisterFailException;
import ns.sos.domain.member.exception.ReissueFailException;
import ns.sos.domain.member.model.MemberFirebaseInfo;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.model.dto.request.MemberCreateRequest;
import ns.sos.domain.member.model.dto.request.MemberLoginRequest;
import ns.sos.domain.member.model.dto.request.MemberReissueRequest;
import ns.sos.domain.member.model.dto.response.MemberLoginResponse;
import ns.sos.domain.member.model.dto.response.MemberReissueTokenResponse;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.member.repository.SmsCertificationDao;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.gugun.repository.GugunRepository;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.domain.region.sido.repository.SidoRepository;
import ns.sos.global.config.security.jwt.JwtToken;
import ns.sos.global.config.security.jwt.JwtTokenProvider;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsCertificationDao smsCertificationDao;
    private final FirebaseService firebaseService;
    private final SidoRepository sidoRepository;
    private final GugunRepository gugunRepository;

    @Override
    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByLoginIdAndStatus(memberLoginRequest.getLoginId(), 'Y')
                .orElseThrow(()->new LoginFailException(ErrorCode.NOT_EXIST_MEMBER,"해당하는 계정이 존재하지 않습니다."));

        if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new LoginFailException(ErrorCode.NOT_MATCH_PASSWORD, "아이디와 비밀번호 정보가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.issue(member.getId(), member.getRole());
        return MemberLoginResponse.from(jwtToken);
    }

    @Override
    public MemberLoginResponse oauthLogin(String email) {
        Member member = memberRepository.findByLoginIdAndStatus(email, 'Y')
                .orElseThrow(()->new OauthLoginFailException(ErrorCode.NOT_EXIST_MEMBER,"해당하는 계정이 존재하지 않습니다. 회원가입이 필요합니다."));

        // JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.issue(member.getId(), member.getRole());

        return MemberLoginResponse.from(jwtToken);
    }

    @Override
    public void register(MemberCreateRequest memberCreateRequest) {
        //todo : 통합 회원일 경우 로직 추가 필요
        String sidoStr = Sido.mappingSido(memberCreateRequest.getSido());
        Sido sido = sidoRepository.findByNameContaining(sidoStr);
        if(sido == null) {
            throw new RegisterFailException(ErrorCode.NOT_EXIST_SIDO);
        }
        Gugun gugun = gugunRepository.findByNameContainingAndSido(memberCreateRequest.getGugun(), sido);
        if(gugun == null) {
            throw new RegisterFailException(ErrorCode.NOT_EXIST_GUGUN);
        }

        boolean isExistMember = memberRepository.existsByLoginId(memberCreateRequest.getLoginId());
        if(isExistMember){
            throw new RegisterFailException(ErrorCode.DUPLICATED_MEMBER, "해당 ID를 가진 회원이 이미 존재합니다.");
        }

        // 전화번호 인증 확인
        String phone = memberCreateRequest.getPhone();
        System.out.println(phone);
        boolean isCertifiedPhone = smsCertificationDao.hasKey(phone);

        if (!isCertifiedPhone) {
            throw new RegisterFailException(ErrorCode.PHONE_NOT_CERTIFIED, "전화번호 인증이 되지 않았습니다.");
        }

        // 회원 등록
        Member member = Member.of(memberCreateRequest, passwordEncoder.encode(memberCreateRequest.getPassword()));
        member.updateIsCertifiedPhone(); // 전화번호 인증 여부 설정

        memberRepository.save(member);

        // 전화번호 인증 기록 삭제 (선택 사항)
        smsCertificationDao.removeSmsCertification(phone);

        //firebase에 요청
        try {
            firebaseService.sendRegisterRequest(new MemberFirebaseInfo(member.getLoginId(), memberCreateRequest.getFcmToken(), String.valueOf(sido.getId()), String.valueOf(gugun.getId())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public MemberReissueTokenResponse reissueToken(MemberReissueRequest memberReissueRequest) {
        if(!jwtTokenProvider.validateToken(memberReissueRequest.getRefreshToken())){
            throw new ReissueFailException(ErrorCode.EXPIRED_REFRESH_TOKEN, "refresh token이 만료되었습니다. 로그인을 해야합니다.");
        }

        return MemberReissueTokenResponse.from(jwtTokenProvider.reissue(memberReissueRequest.getAccessToken()));
    }

    @Override
    public boolean isDuplicateId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }
}
