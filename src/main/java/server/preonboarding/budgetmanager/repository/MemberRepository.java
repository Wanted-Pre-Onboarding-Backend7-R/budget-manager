package server.preonboarding.budgetmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.preonboarding.budgetmanager.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
