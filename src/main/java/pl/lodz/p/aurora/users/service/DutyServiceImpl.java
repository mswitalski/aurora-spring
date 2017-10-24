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
        return dutyRepository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Duty findById(Long id) {
        return dutyRepository.findOne(id);
    }

    @Override
    public Duty create(Duty duty) {
        return save(duty, dutyRepository);
    }

    @Override
    public void update(String eTag, Duty duty) {
        Duty storedDuty = dutyRepository.findOne(duty.getId());

        failIfNoRecordInDatabaseFound(storedDuty, duty);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);

        storedDuty.setName(duty.getName());

        save(storedDuty, dutyRepository);
    }

    @Override
    public void delete(String eTag, Long dutyId) {
        Duty storedDuty = dutyRepository.findOne(dutyId);

        failIfNoRecordInDatabaseFound(storedDuty, dutyId);
        failIfEncounteredOutdatedEntity(eTag, storedDuty);
        dutyRepository.delete(storedDuty);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Page<Duty> search(DutySearchDto critieria, Pageable pageable) {
        return this.dutyRepository.search(critieria.getName(), pageable);
    }
}
