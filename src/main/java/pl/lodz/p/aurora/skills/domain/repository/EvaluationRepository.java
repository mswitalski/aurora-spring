package pl.lodz.p.aurora.skills.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
