package ns.sos.domain.image.repository;

import ns.sos.domain.image.model.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Integer> {

    List<BoardImage> findByBoardId(int boardId);

    Optional<BoardImage> findByAddr(String addr);
}
