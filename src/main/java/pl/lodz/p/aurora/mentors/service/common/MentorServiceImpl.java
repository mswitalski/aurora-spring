package pl.lodz.p.aurora.mentors.service.common;

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

@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public class MentorServiceImpl extends BaseService implements MentorService {

    private final MentorRepository mentorRepository;

    @Autowired
    public MentorServiceImpl(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
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
        return mentorRepository.search(criteria.getSkill(), pageable);
    }
}
