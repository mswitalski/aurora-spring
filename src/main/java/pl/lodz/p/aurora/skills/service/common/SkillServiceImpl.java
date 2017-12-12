package pl.lodz.p.aurora.skills.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.skills.domain.dto.SkillSearchDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;
import pl.lodz.p.aurora.skills.domain.repository.SkillRepository;

import java.util.List;

@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public class SkillServiceImpl extends BaseService implements SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @Override
    public Page<Skill> findAllByPage(Pageable pageable) {
        return skillRepository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Skill findById(Long skillId) {
        Skill storedSkill = skillRepository.findOne(skillId);

        failIfNoRecordInDatabaseFound(storedSkill, skillId);

        return storedSkill;
    }

    @Override
    public Page<Skill> search(SkillSearchDto criteria, Pageable pageable) {
        return skillRepository.search(criteria.getName(), pageable);
    }
}
