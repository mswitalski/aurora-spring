package pl.lodz.p.aurora.msk.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.msk.domain.entity.Skill;
import pl.lodz.p.aurora.msk.domain.repository.SkillRepository;

import java.util.List;

@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
public class SkillServiceImpl extends BaseService implements SkillService {

    private final SkillRepository repository;

    @Autowired
    public SkillServiceImpl(SkillRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Skill> findAll() {
        return repository.findAll();
    }

    @Override
    public Skill findById(Long skillId) {
        Skill storedSkill = repository.findOne(skillId);

        failIfNoRecordInDatabaseFound(storedSkill, skillId);

        return storedSkill;
    }
}
