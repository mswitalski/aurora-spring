package pl.lodz.p.aurora.mme.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.mme.web.dto.MentorSearchDto;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;
import pl.lodz.p.aurora.mme.domain.repository.MentorRepository;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mmeTransactionManager")
public class MentorUnitLeaderServiceImpl extends BaseService implements MentorUnitLeaderService {

    private final MentorRepository repository;

    @Autowired
    public MentorUnitLeaderServiceImpl(MentorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(Long mentorId, String eTag) {
        Mentor storedMentor = repository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        repository.delete(mentorId);
    }

    @Override
    public Page<Mentor> findAllByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Mentor findById(Long mentorId) {
        Mentor storedMentor = repository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);

        return storedMentor;
    }

    @Override
    public Page<Mentor> search(MentorSearchDto criteria, Pageable pageable) {
        return repository.searchAll(criteria.getSkill(), pageable);
    }

    @Override
    public void update(Long mentorId, Mentor mentor, String eTag) {
        Mentor storedMentor = repository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        storedMentor.setActive(mentor.isActive());
        storedMentor.setApproved(mentor.isApproved());
        save(storedMentor, repository);
    }
}
