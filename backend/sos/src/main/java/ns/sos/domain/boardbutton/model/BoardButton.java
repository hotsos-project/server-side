package ns.sos.domain.boardbutton.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_button")
@Entity
public class BoardButton extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(length = 10, nullable = false)
    private String type;

    public BoardButton(Board board, Member member, String type) {
        this.board = board;
        this.member = member;
        this.type = type;
    }

    public void updateType(String type) {
        this.type = type;
    }
}