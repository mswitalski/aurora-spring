package pl.lodz.p.aurora.mentors.service.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.mentors.domain.dto.MentorSearchDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

public interface MentorService {

    Page<Mentor> findAllByPage(Pageable pageable);

    Mentor findById(Long mentorId);

    Page<Mentor> search(MentorSearchDto criteria, Pageable pageable);
}
