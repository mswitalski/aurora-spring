package pl.lodz.p.aurora.msk.service.unitleader;

import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.mus.domain.entity.User;

public interface EvaluationUnitLeaderService {

    Evaluation create(Evaluation evaluation);

    void delete(Long evaluationId, String eTag, User activeUser);

    Evaluation findById(Long evaluationId);

    void update(Long evaluationId, Evaluation evaluation, String eTag);
}
