package pl.lodz.p.aurora.mentors.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.mentors.web.dto.MentorSearchDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

public interface MentorUnitLeaderService {

    void delete(Long mentorId, String eTag);

    Page<Mentor> findAllByPage(Pageable pageable);

    Mentor findById(Long mentorId);

    Page<Mentor> search(MentorSearchDto criteria, Pageable pageable);

    void update(Long mentorId, Mentor mentor, String eTag);
}
