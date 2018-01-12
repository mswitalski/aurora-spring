package pl.lodz.p.aurora.msk.service.common;

import pl.lodz.p.aurora.msk.domain.entity.Skill;

import java.util.List;

public interface SkillService {

    List<Skill> findAll();

    Skill findById(Long skillId);
}
