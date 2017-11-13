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
import pl.lodz.p.aurora.users.domain.dto.DutySearchDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;
import pl.lodz.p.aurora.users.domain.repository.DutyRepository;

import java.util.List;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public class DutyUnitLeaderServiceImpl extends BaseService implements DutyUnitLeaderService {

    private final DutyRepository dutyRepository;

    @Autowired
    public DutyUnitLeaderServiceImpl(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public Duty create(Duty duty) {
        return save(duty, dutyRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void delete(Long dutyId, String eTag) {
        Duty storedDuty = dutyRepository.findOne(dutyId);

        failIfNoRecordInDatabaseFound(storedDuty, dutyId);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);
        dutyRepository.delete(storedDuty);
    }

    @Override
    public List<Duty> findAll() {
        return dutyRepository.findAll();
    }

    @Override
    public Page<Duty> findAllByPage(Pageable pageable) {
        return dutyRepository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    public Duty findById(Long id) {
        return dutyRepository.findOne(id);
    }

    @Override
    public Page<Duty> search(DutySearchDto critieria, Pageable pageable) {
        return this.dutyRepository.search(critieria.getName(), pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void update(Long dutyId, Duty duty, String eTag) {
        Duty storedDuty = dutyRepository.findOne(dutyId);

        failIfNoRecordInDatabaseFound(storedDuty, duty);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);

        storedDuty.setName(duty.getName());

        save(storedDuty, dutyRepository);
    }
}
