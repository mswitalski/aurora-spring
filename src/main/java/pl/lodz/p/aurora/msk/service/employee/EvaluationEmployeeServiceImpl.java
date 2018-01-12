package pl.lodz.p.aurora.msk.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.exception.ActionForbiddenException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.msk.domain.other.SkillLevel;
import pl.lodz.p.aurora.msk.domain.repository.EvaluationRepository;
import pl.lodz.p.aurora.msk.service.common.SkillService;
import pl.lodz.p.aurora.mus.domain.entity.User;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mskTransactionManager")
public class EvaluationEmployeeServiceImpl extends BaseService implements EvaluationEmployeeService {

    private final EvaluationRepository repository;
    private final SkillService skillService;

    @Autowired
    public EvaluationEmployeeServiceImpl(EvaluationRepository repository, SkillService skillService) {
        this.repository = repository;
        this.skillService = skillService;
    }

    @Override
    public Evaluation create(Evaluation evaluation, User employee) {
        evaluation.setUser(employee);
        evaluation.setSkill(skillService.findById(evaluation.getSkill().getId()));
        evaluation.setLeaderEvaluation(SkillLevel.NOT_EVALUATED);
        evaluation.setLeaderExplanation("");

        return save(evaluation, repository);
    }

    @Override
    public void delete(Long evaluationId, User employee, String eTag) {
        Evaluation storedEvaluation = repository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToAccessNotOwnedEvaluation(employee, storedEvaluation);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        repository.delete(evaluationId);
    }

    private void failIfTriedToAccessNotOwnedEvaluation(User employee, Evaluation evaluation) {
        if (!evaluation.getUser().getId().equals(employee.getId())) {
            throw new ActionForbiddenException("Employee tried to change not his evaluation: " + evaluation);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
    public Evaluation findById(Long evaluationId, User employee) {
        Evaluation storedEvaluation = repository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToAccessNotOwnedEvaluation(employee, storedEvaluation);

        return storedEvaluation;
    }

    @Override
    public void update(Long evaluationId, Evaluation evaluation, String eTag, User employee) {
        Evaluation storedEvaluation = repository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToAccessNotOwnedEvaluation(employee, storedEvaluation);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        storedEvaluation.setSelfEvaluation(evaluation.getSelfEvaluation());
        storedEvaluation.setSelfExplanation(evaluation.getSelfExplanation());
        save(storedEvaluation, repository);
    }
}
