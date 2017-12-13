package pl.lodz.p.aurora.mentors.service.employee;

import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.users.domain.entity.User;

public interface MentorEmployeeService {

    Mentor create(Mentor mentor, User employee);

    void delete(Long mentorId, User employee, String eTag);

    void update(Long mentorId, Mentor mentor, String eTag, User employee);
}
