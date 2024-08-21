package ns.sos.domain.notice.repository;

import ns.sos.domain.notice.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    List<Notice> findTop10ByOrderByIdDesc();
    List<Notice> findByTitleContaining(String keyword);
    Optional<Notice> findByIdAndMemberId(Integer id, Integer memberId);
}