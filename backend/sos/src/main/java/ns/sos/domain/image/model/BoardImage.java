package ns.sos.domain.image.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.board.model.Board;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_image")
@Entity
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @NotNull
    private String addr;

    private BoardImage(final Board board, final String addr) {
        this.board = board;
        this.addr = addr;
    }

    public static BoardImage of(final Board board, final String addr) {
        return new BoardImage(board, addr);
    }
}
