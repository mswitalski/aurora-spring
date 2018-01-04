package pl.lodz.p.aurora.skills.service.unitleader;

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
import pl.lodz.p.aurora.users.service.common.UserService;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mskTransactionManager")
public class EvaluationUnitLeaderServiceImpl extends BaseService implements EvaluationUnitLeaderService {

    private final EvaluationRepository repository;
    private final SkillService skillService;
    private final UserService userService;

    @Autowired
    public EvaluationUnitLeaderServiceImpl(EvaluationRepository repository, SkillService skillService, UserService userService) {
        this.repository = repository;
        this.skillService = skillService;
        this.userService = userService;
    }

    @Override
    public Evaluation create(Evaluation evaluation) {
        evaluation.setUser(userService.findById(evaluation.getUser().getId()));
        evaluation.setSkill(skillService.findById(evaluation.getSkill().getId()));
        evaluation.setSelfEvaluation(SkillLevel.NOT_EVALUATED);
        evaluation.setSelfExplanation("");

        return save(evaluation, repository);
    }

    @Override
    public void delete(Long evaluationId, String eTag, User activeUser) {
        Evaluation storedEvaluation = repository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfTriedToModifyOwnedEvaluation(activeUser, storedEvaluation);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        repository.delete(evaluationId);
    }

    private void failIfTriedToModifyOwnedEvaluation(User unitLeader, Evaluation evaluation) {
        if (evaluation.getUser().getId().equals(unitLeader.getId())) {
            throw new ActionForbiddenException("Unit leader tried to delete his evaluation: " + evaluation);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
    public Evaluation findById(Long evaluationId) {
        Evaluation storedEvaluation = repository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);

        return storedEvaluation;
    }

    @Override
    public void update(Long evaluationId, Evaluation evaluation, String eTag) {
        Evaluation storedEvaluation = repository.findOne(evaluationId);

        failIfNoRecordInDatabaseFound(storedEvaluation, evaluationId);
        failIfEncounteredOutdatedEntity(eTag, storedEvaluation);
        storedEvaluation.setLeaderEvaluation(evaluation.getLeaderEvaluation());
        storedEvaluation.setLeaderExplanation(evaluation.getLeaderExplanation());
        save(storedEvaluation, repository);
    }
}
