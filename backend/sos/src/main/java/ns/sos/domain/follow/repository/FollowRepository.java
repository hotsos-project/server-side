package ns.sos.domain.follow.repository;

import java.util.Optional;
import ns.sos.domain.follow.model.Follow;
import ns.sos.domain.member.model.dto.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    List<Follow> findByMember(Member member);

    List<Follow> findByFollowMember(Member member);

    boolean existsByMemberAndFollowMember(Member member, Member followMember);

    Follow findByMemberAndFollowMember(Member member, Member followMember);

    Optional<Follow> findByMemberIdAndFollowMemberId(Integer memberId, Integer followMemberId);

    boolean existsByMemberIdAndFollowMemberId(Integer memberId, Integer followMemberId);

    List<Follow> findByFollowMemberIdAndMemberNicknameContaining(Integer followMemberId, String nickName);

    List<Follow> findByMemberIdAndNickNameContaining(Integer memberId, String nickName);

    int countByMember(Member member);
}

