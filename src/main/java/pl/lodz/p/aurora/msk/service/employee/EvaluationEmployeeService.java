package pl.lodz.p.aurora.msk.service.employee;

import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.mus.domain.entity.User;

public interface EvaluationEmployeeService {

    Evaluation create(Evaluation evaluation, User employee);

    void delete(Long evaluationId, User employee, String eTag);

    Evaluation findById(Long evaluationId, User employee);

    void update(Long evaluationId, Evaluation evaluation, String eTag, User employee);
}
