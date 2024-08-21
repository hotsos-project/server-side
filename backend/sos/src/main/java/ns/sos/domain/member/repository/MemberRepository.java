package ns.sos.domain.member.repository;

import jakarta.persistence.LockModeType;
import ns.sos.domain.member.model.dto.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByLoginIdAndStatus(String loginId, char status);

    boolean existsByLoginId(String loginId);

    @Query("SELECT m FROM Member m WHERE m.nickname LIKE %:keyword% OR m.loginId LIKE %:keyword%")
    List<Member> findByNicknameOrLoginIdContaining(@Param("keyword") String keyword);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Member m WHERE m.id = :id")
    Optional<Member> findByIdForUpdate(@Param("id") Integer id);

}

