package pl.lodz.p.aurora.users.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.web.dto.DutySearchDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;
import pl.lodz.p.aurora.users.domain.repository.DutyRepository;

import java.util.List;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "musTransactionManager")
public class DutyUnitLeaderServiceImpl extends BaseService implements DutyUnitLeaderService {

    private final DutyRepository repository;

    @Autowired
    public DutyUnitLeaderServiceImpl(DutyRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
    public Duty create(Duty duty) {
        return save(duty, repository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
    public void delete(Long dutyId, String eTag) {
        Duty storedDuty = repository.findOne(dutyId);

        failIfNoRecordInDatabaseFound(storedDuty, dutyId);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);
        repository.delete(storedDuty);
    }

    @Override
    public List<Duty> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Duty> findAllByPage(Pageable pageable) {
        return repository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    public Duty findById(Long id) {
        Duty storedDuty = repository.findOne(id);

        failIfNoRecordInDatabaseFound(storedDuty, id);

        return storedDuty;
    }

    @Override
    public Page<Duty> search(DutySearchDto critieria, Pageable pageable) {
        return repository.search(critieria.getName(), pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
    public void update(Long dutyId, Duty duty, String eTag) {
        Duty storedDuty = repository.findOne(dutyId);

        failIfNoRecordInDatabaseFound(storedDuty, duty);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);
        storedDuty.setName(duty.getName());
        save(storedDuty, repository);
    }
}
