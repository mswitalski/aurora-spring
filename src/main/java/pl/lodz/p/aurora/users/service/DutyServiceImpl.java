package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.domain.entity.Duty;
import pl.lodz.p.aurora.users.domain.repository.DutyRepository;

@Service
public class DutyServiceImpl extends BaseService implements DutyService {

    private final DutyRepository dutyRepository;

    @Autowired
    public DutyServiceImpl(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    @Override
    public Page<Duty> findAllByPage(Pageable pageable) {
        return dutyRepository.findAll(pageable);
    }

    @Override
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
}
