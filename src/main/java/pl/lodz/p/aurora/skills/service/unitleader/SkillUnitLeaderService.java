package pl.lodz.p.aurora.skills.service.unitleader;

import pl.lodz.p.aurora.skills.domain.entity.Skill;

public interface SkillUnitLeaderService {

    Skill create(Skill skill);

    void delete(Long skillId, String eTag);

    Skill findById(Long skillId);

    void update(Long skillId, Skill skill, String eTag);
}
