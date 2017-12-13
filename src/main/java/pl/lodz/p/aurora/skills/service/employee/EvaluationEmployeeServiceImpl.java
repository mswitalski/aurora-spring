package pl.lodz.p.aurora.skills.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.exception.ActionForbiddenException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.skills.domain.other.SkillLevel;
import pl.lodz.p.aurora.skills.domain.repository.EvaluationRepository;
import pl.lodz.p.aurora.skills.service.common.SkillService;
import pl.lodz.p.aurora.users.domain.entity.User;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class EvaluationEmployeeServiceImpl extends BaseService implements EvaluationEmployeeService {

    private final EvaluationRepository evaluationRepository;
    private final SkillService skillService;

    @Autowired
    public EvaluationEmployeeServiceImpl(EvaluationRepository evaluationRepository, SkillService skillService) {
        this.evaluationRepository = evaluationRepository;
        this.skillService = skillService;
    }

    @Override
    public Evaluation create(Evaluation evaluation, User employee) {
        evaluation.setUser(employee);
        evaluation.setSkill(skillService.findById(evaluation.getSkill().getId()));
        evaluation.setLeaderEvaluation(SkillLevel.NOT_EVALUATED);
        evaluation.setLeaderExplanation("");

        return save(evaluation, evaluationRepository);
    }

    @Override
    public void delete(Long evaluationId, User employee, String eTag) {
        Evaluation storedEvaluation = evaluationRepository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToAccessNotOwnedEvaluation(employee, storedEvaluation);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        evaluationRepository.delete(evaluationId);
    }

    private void failIfTriedToAccessNotOwnedEvaluation(User employee, Evaluation evaluation) {
        if (!evaluation.getUser().getId().equals(employee.getId())) {
            throw new ActionForbiddenException("Employee tried to change not his evaluation: " + evaluation);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Evaluation findById(Long evaluationId, User employee) {
        Evaluation storedEvaluation = evaluationRepository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToAccessNotOwnedEvaluation(employee, storedEvaluation);

        return storedEvaluation;
    }

    @Override
    public void update(Long evaluationId, Evaluation evaluation, String eTag, User employee) {
        Evaluation storedEvaluation = evaluationRepository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToAccessNotOwnedEvaluation(employee, storedEvaluation);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        storedEvaluation.setSelfEvaluation(evaluation.getSelfEvaluation());
        storedEvaluation.setSelfExplanation(evaluation.getSelfExplanation());
        save(storedEvaluation, evaluationRepository);
    }
}
