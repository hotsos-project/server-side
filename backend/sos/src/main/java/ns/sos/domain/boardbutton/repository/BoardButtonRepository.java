package ns.sos.domain.boardbutton.repository;

import ns.sos.domain.boardbutton.model.BoardButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardButtonRepository extends JpaRepository<BoardButton, Integer> {

    BoardButton findByBoardIdAndMemberId(Integer boardId, Integer memberId);
}