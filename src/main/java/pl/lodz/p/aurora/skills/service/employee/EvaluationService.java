package pl.lodz.p.aurora.skills.service.employee;

import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

public interface EvaluationService {

    Evaluation create(Evaluation evaluation, User employee);

    void delete(Long evaluationId, User employee, String eTag);

    List<Evaluation> findEmployeeEvaluations(User employee);

    void update(Long evaluationId, Evaluation evaluation, String eTag);
}
