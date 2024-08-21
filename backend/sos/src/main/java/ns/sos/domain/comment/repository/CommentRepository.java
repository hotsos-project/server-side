package ns.sos.domain.comment.repository;

import ns.sos.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.status = 'Y'")
    List<Comment> findCommentsByBoardIdAndStatusY(@Param("boardId") final Integer boardId);
}
