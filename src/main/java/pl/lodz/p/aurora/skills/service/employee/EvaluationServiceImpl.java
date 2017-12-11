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
import pl.lodz.p.aurora.skills.domain.repository.EvaluationRepository;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class EvaluationServiceImpl extends BaseService implements EvaluationService {

    private final EvaluationRepository evaluationRepository;

    @Autowired
    public EvaluationServiceImpl(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public Evaluation create(Evaluation evaluation, User employee) {
        evaluation.setUser(employee);

        return save(evaluation, evaluationRepository);
    }

    @Override
    public void delete(Long evaluationId, User employee, String eTag) {
        Evaluation storedEvaluation = evaluationRepository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToDeleteNotOwnedEvaluation(employee, storedEvaluation);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        evaluationRepository.delete(evaluationId);
    }

    private void failIfTriedToDeleteNotOwnedEvaluation(User employee, Evaluation evaluation) {
        if (!evaluation.getUser().equals(employee)) {
            throw new ActionForbiddenException("Employee tried to change not his evaluation for user: " + evaluation.getUser().getUsername());
        }
    }

    public List<Evaluation> findEmployeeEvaluations(User employee) {
        return evaluationRepository.findAllByUser(employee);
    }

    @Override
    public void update(Long evaluationId, Evaluation evaluation, String eTag) {
        Evaluation storedEvaluation = evaluationRepository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        storedEvaluation.setSelfEvaluation(evaluation.getSelfEvaluation());
        storedEvaluation.setSelfExplanation(evaluation.getSelfExplanation());
        save(storedEvaluation, evaluationRepository);
    }
}
