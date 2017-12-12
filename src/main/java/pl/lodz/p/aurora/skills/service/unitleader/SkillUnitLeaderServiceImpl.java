package pl.lodz.p.aurora.skills.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.skills.domain.entity.Skill;
import pl.lodz.p.aurora.skills.domain.repository.SkillRepository;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class SkillUnitLeaderServiceImpl extends BaseService implements SkillUnitLeaderService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillUnitLeaderServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill create(Skill skill) {
        return save(skill, skillRepository);
    }

    @Override
    public void delete(Long skillId, String eTag) {
        Skill storedSkill = skillRepository.findOne(skillId);

        failIfNoRecordInDatabaseFound(storedSkill, skillId);
        failIfEncounteredOutdatedEntity(eTag, storedSkill);
        skillRepository.delete(skillId);
    }

    @Override
    public void update(Long skillId, Skill skill, String eTag) {
        Skill storedSkill = skillRepository.findOne(skillId);

        failIfNoRecordInDatabaseFound(storedSkill, skillId);
        failIfEncounteredOutdatedEntity(eTag, storedSkill);
        storedSkill.setName(skill.getName());
        save(storedSkill, skillRepository);
    }
}
