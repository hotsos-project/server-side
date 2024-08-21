package ns.sos.domain.follow.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.follow.model.Follow;
import ns.sos.domain.follow.model.dto.FollowFirebaseInfo;
import ns.sos.domain.follow.model.dto.request.FollowNickNameRequest;
import ns.sos.domain.follow.model.dto.response.FollowPhoneResponse;
import ns.sos.domain.follow.model.dto.response.FollowResponse;
import ns.sos.domain.follow.repository.FollowRepository;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import ns.sos.global.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final FirebaseService firebaseService;

    public void follow(final Integer memberId, final Integer followerId) {
        Member member = getMemberForUpdateById(memberId);
        Member followMember = getMemberForUpdateById(followerId);

        validateFollowMembers(member, followMember);

        if (isFollowing(member, followMember)) { // 이미 팔로우 상태
            unfollow(member, followMember);
            try {
                firebaseService.sendFollowRequest(new FollowFirebaseInfo(member.getLoginId(), followMember.getLoginId(), "Y"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (followRepository.countByMember(member) >= 10) {
                throw new BusinessException(ErrorCode.FOLLOW_OVER_SIZE);
            }
            follow(member, followMember);
            try {
                firebaseService.sendFollowRequest(new FollowFirebaseInfo(member.getLoginId(), followMember.getLoginId(), "N"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowers(final Integer memberId) {
        Member member = getMemberById(memberId);

        return followRepository.findByFollowMember(member)
                .stream()
                .map(follow -> FollowResponse.of(follow.getId(), follow.getMember(), isSafe(follow.getFollowMember().getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowings(final Integer memberId) {
        Member member = getMemberById(memberId);

        return followRepository.findByMember(member)
                .stream()
                .map(follow -> FollowResponse.of(follow.getId(), follow.getFollowMember(), follow.getNickName(), isSafe(follow.getFollowMember().getId())))
                .toList();
    }

    public FollowResponse updateFollowNickName(final Integer memberId, final FollowNickNameRequest followNickNameRequest) {
        Follow follow = followRepository.findByMemberIdAndFollowMemberId(memberId, followNickNameRequest.getFollowerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOW_NOT_FOUND));

        follow.updateNickName(followNickNameRequest.getNickName());
        return FollowResponse.of(follow.getId(), follow.getFollowMember(), follow.getNickName(), isSafe(memberId));
    }

    public FollowPhoneResponse getFollowNumber(final Integer memberId, final Integer followerId) {
        validateMutualFollow(memberId, followerId);

        Member member = getMemberById(memberId);
        return FollowPhoneResponse.from(member);
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> searchFollowersByNickName(final Integer memberId, final String nickName) {
        return followRepository.findByFollowMemberIdAndMemberNicknameContaining(memberId, nickName)
                .stream()
                .map(follow -> FollowResponse.of(follow.getId(), follow.getMember(), isSafe(follow.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> searchFollowingsByNickName(final Integer memberId, final String nickName) {
        return followRepository.findByMemberIdAndNickNameContaining(memberId, nickName)
                .stream()
                .map(follow -> FollowResponse.of(follow.getId(), follow.getFollowMember(), follow.getNickName(), isSafe(follow.getId())))
                .toList();
    }

    private Member getMemberById(final Integer memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MEMBER));
    }

    public Member getMemberForUpdateById(final Integer memberId) {
        return memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MEMBER));
    }

    private void validateFollowMembers(final Member member, final Member followMember) {
        if (member.equals(followMember)) {
            throw new BusinessException(ErrorCode.FOLLOW_BAD_REQUEST);
        }
    }

    private boolean isFollowing(final Member member, final Member followMember) {
        return followRepository.existsByMemberAndFollowMember(member, followMember);
    }

    private void unfollow(final Member member, final Member followMember) {
        Follow follow = followRepository.findByMemberAndFollowMember(member, followMember);
        followRepository.delete(follow);
    }

    private void follow(final Member member, final Member followMember) {
        Follow follow = Follow.of(member, followMember);
        followRepository.save(follow);
    }

    private void validateMutualFollow(final Integer memberId, final Integer followerId) {
        boolean following = followRepository.existsByMemberIdAndFollowMemberId(memberId, followerId);
        boolean follower = followRepository.existsByMemberIdAndFollowMemberId(followerId, memberId);

        if (!following || !follower) {
            throw new BusinessException(ErrorCode.FOLLOW_NOT_MUTUAL);
        }
    }

    private boolean isSafe(final Integer memberId) {
        return redisUtil.getData("safe-" + memberId) != null;
    }
}
