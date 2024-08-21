package ns.sos.domain.notice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
@Entity
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(nullable = false)
    private char status = 'Y';

    public Notice(final Member member, final String title, final String content, final char status) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public static Notice of(final Member member, final String title, final String content, final char status) {
        return new Notice(member, title, content, status);
    }

    public void updateNotice(final String title,final String content) {
        this.title = title;
        this.content = content;
    }
}