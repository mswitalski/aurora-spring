package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.domain.dto.DutySearchDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;
import pl.lodz.p.aurora.users.domain.repository.DutyRepository;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class DutyServiceImpl extends BaseService implements DutyService {

    private final DutyRepository dutyRepository;

    @Autowired
    public DutyServiceImpl(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Page<Duty> findAllByPage(Pageable pageable) {
        return dutyRepository.findAll(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Duty findById(Long id) {
        return dutyRepository.findOne(id);
    }

    @Override
    public Duty createAsAdmin(Duty duty) {
        return save(duty, dutyRepository);
    }

    @Override
    public Duty createAsUnitLeader(Duty duty) {
        return save(duty, dutyRepository);
    }

    @Override
    public void updateAsAdmin(String eTag, Duty duty) {
        update(eTag, duty);
    }

    private void update(String eTag, Duty duty) {
        Duty storedDuty = dutyRepository.findOne(duty.getId());

        failIfNoRecordInDatabaseFound(storedDuty, duty);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);

        save(duty, dutyRepository);
    }

    @Override
    public void updateAsUnitLeader(String eTag, Duty duty) {
        update(eTag, duty);
    }

    @Override
    public void deleteAsAdmin(String eTag, Long dutyId) {
        delete(eTag, dutyId);
    }

    private void delete(String eTag, Long dutyId) {
        Duty storedDuty = dutyRepository.findOne(dutyId);

        failIfNoRecordInDatabaseFound(storedDuty, dutyId);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);
        dutyRepository.delete(storedDuty);
    }

    @Override
    public void deleteAsUnitLeader(String eTag, Long dutyId) {
        delete(eTag, dutyId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Page<Duty> searchForDuties(DutySearchDto critieria, Pageable pageable) {
        return this.dutyRepository.findAllByNameIsLike(critieria.getName(), pageable);
    }
}
