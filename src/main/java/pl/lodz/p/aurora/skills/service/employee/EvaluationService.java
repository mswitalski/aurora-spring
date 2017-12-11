package pl.lodz.p.aurora.skills.service.employee;

import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.users.domain.entity.User;

public interface EvaluationService {

    Evaluation create(Evaluation evaluation, User employee);

    void delete(Long evaluationId, User employee, String eTag);

    void update(Long evaluationId, Evaluation evaluation, String eTag);
}
