package pl.lodz.p.aurora.mme.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;
import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, transactionManager = "mmeTransactionManager")
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByMentorAndUser(Mentor mentor, User user);
}
