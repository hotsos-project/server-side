package ns.sos.domain.favoriteregion.service;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.favoriteregion.model.FavoriteRegion;
import ns.sos.domain.favoriteregion.model.dto.request.FavoriteRegionFirebaseInfo;
import ns.sos.domain.favoriteregion.model.dto.request.FavoriteRegionRequest;
import ns.sos.domain.favoriteregion.model.dto.response.FavoriteRegionResponse;
import ns.sos.domain.favoriteregion.repository.FavoriteRegionRepository;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.gugun.repository.GugunRepository;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.domain.region.sido.repository.SidoRepository;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteRegionService {

    private final MemberRepository memberRepository;
    private final FavoriteRegionRepository favoriteRegionRepository;
    private final SidoRepository sidoRepository;
    private final GugunRepository gugunRepository;
    private final FirebaseService firebaseService;

    public void addRegion(Integer memberId, FavoriteRegionRequest favoriteRegionRequest) {
        Member member = getMember(memberId);
        Sido sido = getSido(favoriteRegionRequest);
        Gugun gugun = getGugun(favoriteRegionRequest);

        // 지역 추가는 1개만 가능함
        if (favoriteRegionRepository.existsByMemberId(memberId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        try {
            firebaseService.sendRegionRequest(
                    new FavoriteRegionFirebaseInfo(sido.getId().toString(), gugun.getId().toString(),
                            member.getLoginId(), "Y"));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        favoriteRegionRepository.save(FavoriteRegion.of(member, sido, gugun));
    }

    public void updateRegion(Integer memberId, Integer regionId, FavoriteRegionRequest favoriteRegionRequest) {
        FavoriteRegion favoriteRegion = favoriteRegionRepository.findById(regionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));

        // 회원의 관심지역이 맞는지 확인
        validateMemberRegion(memberId, favoriteRegion);

        Member member = getMember(memberId);
        Sido sido = getSido(favoriteRegionRequest);
        Gugun gugun = getGugun(favoriteRegionRequest);

        favoriteRegion.updateRegion(sido, gugun);

        try {
            firebaseService.sendRegionRequest(
                    new FavoriteRegionFirebaseInfo(sido.getId().toString(), gugun.getId().toString(),
                            member.getLoginId(), "Y"));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        favoriteRegionRepository.save(favoriteRegion);
    }

    public void deleteRegion(Integer memberId, Integer regionId) {
        FavoriteRegion favoriteRegion = favoriteRegionRepository.findById(regionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));

        // 회원의 관심지역이 맞는지 확인
        validateMemberRegion(memberId, favoriteRegion);

        try {
            firebaseService.sendRegionRequest(
                    new FavoriteRegionFirebaseInfo(favoriteRegion.getSido().getId().toString(),
                            favoriteRegion.getGugun().getId().toString(),
                            favoriteRegion.getMember().getLoginId(),
                            "N"));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        favoriteRegionRepository.delete(favoriteRegion);
    }

    @Transactional(readOnly = true)
    public FavoriteRegionResponse getRegions(Integer memberId) {
        FavoriteRegion favoriteRegion = favoriteRegionRepository.findByMemberId(memberId)
                .getFirst();

        return FavoriteRegionResponse.from(favoriteRegion);
    }

    private Member getMember(Integer memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MEMBER));
    }

    private Sido getSido(FavoriteRegionRequest favoriteRegionRequest) {
        return sidoRepository.findById(favoriteRegionRequest.getSidoId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));
    }

    private Gugun getGugun(FavoriteRegionRequest favoriteRegionRequest) {
        return gugunRepository.findById(favoriteRegionRequest.getGugunId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));
    }

    private void validateMemberRegion(Integer memberId, FavoriteRegion favoriteRegion) {
        if (!favoriteRegion.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
