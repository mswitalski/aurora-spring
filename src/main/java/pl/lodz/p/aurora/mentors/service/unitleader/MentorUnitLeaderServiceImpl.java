package pl.lodz.p.aurora.mentors.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.mentors.domain.dto.MentorSearchDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.mentors.domain.repository.MentorRepository;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class MentorUnitLeaderServiceImpl extends BaseService implements MentorUnitLeaderService {

    private final MentorRepository mentorRepository;

    @Autowired
    public MentorUnitLeaderServiceImpl(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    @Override
    public void delete(Long mentorId, String eTag) {
        Mentor storedMentor = mentorRepository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        mentorRepository.delete(mentorId);
    }

    @Override
    public Page<Mentor> findAllByPage(Pageable pageable) {
        return mentorRepository.findAll(pageable);
    }

    @Override
    public Mentor findById(Long mentorId) {
        Mentor storedMentor = mentorRepository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);

        return storedMentor;
    }

    @Override
    public Page<Mentor> search(MentorSearchDto criteria, Pageable pageable) {
        return mentorRepository.searchAll(criteria.getSkill(), pageable);
    }

    @Override
    public void update(Long mentorId, Mentor mentor, String eTag) {
        Mentor storedMentor = mentorRepository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        storedMentor.setActive(mentor.isActive());
        storedMentor.setApproved(mentor.isApproved());
        save(storedMentor, mentorRepository);
    }
}
