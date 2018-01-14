package pl.lodz.p.aurora.msk.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.msk.web.dto.SkillSearchDto;
import pl.lodz.p.aurora.msk.domain.entity.Skill;
import pl.lodz.p.aurora.msk.domain.repository.SkillRepository;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mskTransactionManager")
public class SkillUnitLeaderServiceImpl extends BaseService implements SkillUnitLeaderService {

    private final SkillRepository repository;

    @Autowired
    public SkillUnitLeaderServiceImpl(SkillRepository repository) {
        this.repository = repository;
    }

    @Override
    public Skill create(Skill skill) {
        return save(skill, repository);
    }

    @Override
    public void delete(Long skillId, String eTag) {
        Skill storedSkill = repository.findOne(skillId);

        failIfNoRecordInDatabaseFound(storedSkill, skillId);
        failIfEncounteredOutdatedEntity(eTag, storedSkill);
        repository.delete(skillId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
    public Page<Skill> findAllByPage(Pageable pageable) {
        return repository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
    public Page<Skill> search(SkillSearchDto criteria, Pageable pageable) {
        return repository.search(criteria.getName(), pageable);
    }

    @Override
    public void update(Long skillId, Skill skill, String eTag) {
        Skill storedSkill = repository.findOne(skillId);

        failIfNoRecordInDatabaseFound(storedSkill, skillId);
        failIfEncounteredOutdatedEntity(eTag, storedSkill);
        storedSkill.setName(skill.getName());
        save(storedSkill, repository);
    }
}
