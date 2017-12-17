package pl.lodz.p.aurora.mentors.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.mentors.domain.entity.Feedback;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ)
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByMentorAndUser(Mentor mentor, User user);
}
