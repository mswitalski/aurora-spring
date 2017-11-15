package pl.lodz.p.aurora.skills.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.skills.domain.dto.SkillSearchDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

import java.util.List;

public interface SkillUnitLeaderService {

    Skill create(Skill skill);

    void delete(Long skillId, String eTag);

    List<Skill> findAll();

    Page<Skill> findAllByPage(Pageable pageable);

    Skill findById(Long skillId);

    Page<Skill> search(SkillSearchDto criteria, Pageable pageable);

    void update(Long skillId, Skill skill, String eTag);
}
