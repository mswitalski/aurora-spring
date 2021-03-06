package pl.lodz.p.aurora.mme.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.exception.ActionForbiddenException;
import pl.lodz.p.aurora.msh.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.mme.web.dto.MentorSearchDto;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;
import pl.lodz.p.aurora.mme.domain.repository.MentorRepository;
import pl.lodz.p.aurora.mme.exception.IncompetentMentorException;
import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.msk.domain.other.SkillLevel;
import pl.lodz.p.aurora.msk.service.employee.EvaluationEmployeeService;
import pl.lodz.p.aurora.mus.domain.entity.User;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mmeTransactionManager")
public class MentorEmployeeServiceImpl extends BaseService implements MentorEmployeeService {

    private final MentorRepository repository;
    private final EvaluationEmployeeService evaluationService;

    @Autowired
    public MentorEmployeeServiceImpl(MentorRepository repository, EvaluationEmployeeService evaluationService) {
        this.repository = repository;
        this.evaluationService = evaluationService;
    }

    @Override
    public Mentor create(Mentor mentor, User employee) {
        Evaluation storedEvaluation = evaluationService.findById(mentor.getEvaluation().getId(), employee);

        failOnIncompetentUser(employee, storedEvaluation);
        mentor.setEvaluation(storedEvaluation);
        mentor.setApproved(false);
        mentor.setActive(true);

        return save(mentor, repository);
    }

    private void failOnIncompetentUser(User employee, Evaluation evaluation) {
        SkillLevel level = evaluation.getLeaderEvaluation();

        if (level != SkillLevel.EXPERT && level != SkillLevel.INTERMEDIATE) {
            throw new IncompetentMentorException("Employee " + employee.getUsername()
                    + " tried to become a mentor in " + evaluation.getSkill().getName()
                    + " but had insufficient level of the skill");
        }
    }

    @Override
    public void delete(Long mentorId, User employee, String eTag) {
        Mentor storedMentor = repository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfTriedToAccessNotOwnedMentor(employee, storedMentor);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        repository.delete(mentorId);
    }

    private void failIfTriedToAccessNotOwnedMentor(User employee, Mentor mentor) {
        if (!mentor.getEvaluation().getUser().getId().equals(employee.getId())) {
            throw new ActionForbiddenException("Employee tried to change not his mentor: " + mentor);
        }
    }

    @Override
    public Page<Mentor> findAllByPage(Pageable pageable) {
        return repository.findAllByActiveTrueAndApprovedTrue(pageable);
    }

    @Override
    public Mentor findById(Long mentorId, User employee) {
        Mentor storedMentor = repository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfAccessedSomebodyElsesDormantMentor(storedMentor, employee);

        return storedMentor;
    }

    private void failIfAccessedSomebodyElsesDormantMentor(Mentor mentor, User employee) {
        if (!mentor.getEvaluation().getUser().getId().equals(employee.getId()) &&
                (!mentor.isActive() || !mentor.isApproved())) {
            throw new InvalidResourceRequestedException("User " + employee
                    + " tries to access somebody else's dormant mentor: " + mentor);
        }
    }

    @Override
    public Page<Mentor> search(MentorSearchDto criteria, Pageable pageable) {
        return repository.searchActive(criteria.getSkill(), pageable);
    }

    @Override
    public void update(Long mentorId, Mentor mentor, String eTag, User employee) {
        Mentor storedMentor = repository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfTriedToAccessNotOwnedMentor(employee, storedMentor);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        storedMentor.setActive(mentor.isActive());
        save(storedMentor, repository);
    }
}
