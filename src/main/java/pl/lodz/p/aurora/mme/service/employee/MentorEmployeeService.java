package pl.lodz.p.aurora.mme.service.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.mme.web.dto.MentorSearchDto;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;
import pl.lodz.p.aurora.mus.domain.entity.User;

public interface MentorEmployeeService {

    Mentor create(Mentor mentor, User employee);

    void delete(Long mentorId, User employee, String eTag);

    Page<Mentor> findAllByPage(Pageable pageable);

    Mentor findById(Long mentorId, User employee);

    Page<Mentor> search(MentorSearchDto criteria, Pageable pageable);

    void update(Long mentorId, Mentor mentor, String eTag, User employee);
}
