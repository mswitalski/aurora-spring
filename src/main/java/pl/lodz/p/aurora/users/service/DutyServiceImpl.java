package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.aurora.users.domain.entity.Duty;
import pl.lodz.p.aurora.users.domain.repository.DutyRepository;

@Service
public class DutyServiceImpl implements DutyService {

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
}
