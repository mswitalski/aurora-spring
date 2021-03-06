package pl.lodz.p.aurora.mme.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.mme.domain.repository.FeedbackRepository;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mmeTransactionManager")
public class FeedbackUnitLeaderServiceImpl extends BaseService implements FeedbackUnitLeaderService {

    private final FeedbackRepository repository;

    @Autowired
    public FeedbackUnitLeaderServiceImpl(FeedbackRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(Long feedbackId) {
        Feedback storedFeedback = repository.findOne(feedbackId);

        failIfNoRecordInDatabaseFound(storedFeedback, feedbackId);
        repository.delete(feedbackId);
    }
}
