package pl.lodz.p.aurora.mentors.service.unitleader;

import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

public interface MentorUnitLeaderService {

    void delete(Long mentorId, String eTag);

    void update(Long mentorId, Mentor mentor, String eTag);
}
