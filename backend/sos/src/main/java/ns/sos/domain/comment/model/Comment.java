package ns.sos.domain.comment.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private char status = 'Y'; // 삭제 아니면 Y

    private Comment(final Member member, final Board board, final String content) {
        this.member = member;
        this.board = board;
        this.content = content;
    }

    public static Comment of(final Member member, final Board board, final String content) {
        return new Comment(member, board, content);
    }


    /**
     * Comment content 수정 메서드
     */
    public void updateContent(final String content) {
        this.content = content;
    }

    /**
     * Comment delete 요청시 상태값 수정 메서드
     */
    public void updateStatus() {
        this.status = 'N';
    }
}