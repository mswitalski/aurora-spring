package pl.lodz.p.aurora.skills.service.common;

import pl.lodz.p.aurora.skills.domain.entity.Skill;

import java.util.List;

public interface SkillService {

    List<Skill> findAll();

    Skill findById(Long skillId);
}
