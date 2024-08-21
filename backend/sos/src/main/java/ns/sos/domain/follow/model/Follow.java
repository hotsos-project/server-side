package ns.sos.domain.follow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
@Entity
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_member_id", nullable = false)
    private Member followMember;

    @NotNull
    private String nickName;

    private Follow(final Member member, final Member followMember) {
        this.member = member;
        this.followMember = followMember;
        nickName = followMember.getNickname();
    }

    public static Follow of(final Member member, final Member followMember) {
        return new Follow(member, followMember);
    }

    public void updateNickName(final String nickName) {
        this.nickName = nickName;
    }
}
