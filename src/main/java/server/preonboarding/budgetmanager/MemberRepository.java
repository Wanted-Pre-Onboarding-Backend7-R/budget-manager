package server.preonboarding.budgetmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import server.preonboarding.budgetmanager.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
