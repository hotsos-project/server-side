package ns.sos.domain.reply.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.comment.model.Comment;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reply")
@Entity
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private char status = 'Y';

    private Reply(final Member member, final Comment comment, final String content) {
        this.member = member;
        this.comment = comment;
        this.content = content;
    }

    public static Reply of(final Member member, final Comment comment, final String content) {
        return new Reply(member, comment, content);
    }

    public void updateComment(final String content) {
        this.content = content;
    }

    public void updateStatus() {
        this.status = 'N';
    }
}