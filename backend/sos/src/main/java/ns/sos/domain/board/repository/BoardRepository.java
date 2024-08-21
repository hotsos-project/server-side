package ns.sos.domain.board.repository;

import jakarta.persistence.LockModeType;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.region.sido.model.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

//    @Query("SELECT b FROM board b WHERE b.status = 'Y' ORDER BY b.id DESC")
//    List<Board> findTop10ByOrderByIdDesc();
//
//    @Query("SELECT b FROM board b WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.status = 'Y'")
//    List<Board> findByTitleOrContentContaining(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM board b WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.status = 'Y' AND (:lastId IS NULL OR b.id < :lastId) ORDER BY b.id DESC LIMIT :limit", nativeQuery = true)
    List<Board> findByTitleOrContentContainingWithPagination(@Param("keyword") String keyword, @Param("lastId") Integer lastId, @Param("limit") int limit);

    @Query(value = "SELECT * FROM board b WHERE b.member_id = :memberId AND b.status = 'Y' AND (:lastId IS NULL OR b.id < :lastId) ORDER BY b.id DESC LIMIT :limit", nativeQuery = true)
    List<Board> findByMemberIdAndStatusWithPagination(@Param("memberId") Integer memberId,@Param("lastId") Integer lastId, @Param("limit") int limit);


    @Query(value = "SELECT * FROM board b WHERE b.status = 'Y' AND (:lastId IS NULL OR b.id < :lastId) ORDER BY b.id DESC LIMIT :limit", nativeQuery = true)
    List<Board> findNextByIdDesc(@Param("lastId") Integer lastId, @Param("limit") int limit);

    List<Board> findTop10BySidoAndStatusOrderByCreatedAtDesc(Sido Sido, char status);

    @Query(value = "SELECT * FROM board b WHERE b.sido_id = :sidoId AND b.status = 'Y' AND (:lastId IS NULL OR b.id < :lastId) ORDER BY b.createdAt DESC LIMIT :limit", nativeQuery = true)
    List<Board> findTop10BySidoAndStatusOrderByCreatedAtDescWithPagination(@Param("sidoId") Integer sidoId, @Param("lastId") Integer lastId, @Param("limit") int limit);


    @Query("SELECT b FROM Board b WHERE b.createdAt > :oneDayAgo AND b.status='Y'" +
            "AND b.isAlarm = true ORDER BY (0.7 * b.factCnt + 0.3 * b.count) DESC LIMIT 3")
    List<Board> findTop3ByCustomScore(@Param("oneDayAgo") LocalDateTime oneDayAgo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Board b WHERE b.id = :id AND b.status = 'Y'")
    Optional<Board> findByIdAndStatusForUpdate(@Param("id") Integer id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM Board b WHERE b.id = :id AND b.status = 'Y'")
    Optional<Board> findByIdWithOptimisticLock(@Param("id") Integer id);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.member m " +
            "JOIN FETCH b.sido s " +
            "JOIN FETCH b.gugun g " +
            "WHERE b.id = :id")
    Optional<Board> findByIdWithMemberAndLocation(@Param("id") Integer id);

}