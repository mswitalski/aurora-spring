package pl.lodz.p.aurora.mentors.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.mentors.domain.entity.Feedback;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ)
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
