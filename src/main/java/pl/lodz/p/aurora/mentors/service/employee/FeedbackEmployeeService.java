package pl.lodz.p.aurora.mentors.service.employee;

import pl.lodz.p.aurora.mentors.domain.entity.Feedback;
import pl.lodz.p.aurora.users.domain.entity.User;

public interface FeedbackEmployeeService {

    Feedback create(Feedback feedback, User employee);
}
