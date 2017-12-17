package pl.lodz.p.aurora.mentors.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.mentors.domain.entity.Feedback;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.mentors.domain.repository.FeedbackRepository;
import pl.lodz.p.aurora.mentors.exception.SelfFeedbackException;
import pl.lodz.p.aurora.mentors.exception.TooFrequentFeedbackException;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.time.LocalDate;
import java.util.List;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class FeedbackEmployeeServiceImpl extends BaseService implements FeedbackEmployeeService {

    private final FeedbackRepository repository;
    private final MentorEmployeeService mentorService;

    @Autowired
    public FeedbackEmployeeServiceImpl(FeedbackRepository repository, MentorEmployeeService mentorService) {
        this.repository = repository;
        this.mentorService = mentorService;
    }

    @Override
    public Feedback create(Feedback feedback, User employee) {
        Mentor storedMentor = mentorService.findById(feedback.getMentor().getId(), employee);

        failOnProvidingFeedbackForOwnMentor(storedMentor, employee);
        failOnMoreThanOneFeedbackPerDay(storedMentor, employee);
        feedback.setMentor(storedMentor);
        feedback.setUser(employee);
        feedback.setCreateDateTime(null);

        return save(feedback, repository);
    }

    private void failOnProvidingFeedbackForOwnMentor(Mentor mentor, User employee) {
        if (mentor.getEvaluation().getUser().getId().equals(employee.getId())) {
            throw new SelfFeedbackException("User " + employee + " tried to give himself a feedback on mentoring");
        }
    }

    private void failOnMoreThanOneFeedbackPerDay(Mentor mentor, User employee) {
        List<Feedback> feedback = repository.findAllByMentorAndUser(mentor, employee);

        if (feedback.stream().anyMatch(f -> f.getUser().getId().equals(employee.getId())
                && f.getCreateDateTime().toLocalDate().equals(LocalDate.now()))) {
            throw new TooFrequentFeedbackException("User " + employee
                    + " tried to give more than one feedback per day for mentor " + mentor);
        }
    }
}
