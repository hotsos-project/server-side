package ns.sos.domain.reply.repository;

import ns.sos.domain.reply.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Query("SELECT r FROM Reply r WHERE r.comment.id = :commentId AND r.status = 'Y'")
    List<Reply> findRepliesByBoardIdAndStatusY(@Param("commentId") final Integer commentId);
}
