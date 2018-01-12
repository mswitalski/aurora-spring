package pl.lodz.p.aurora.mme.service.employee;

import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.mus.domain.entity.User;

public interface FeedbackEmployeeService {

    Feedback create(Feedback feedback, User employee);
}
