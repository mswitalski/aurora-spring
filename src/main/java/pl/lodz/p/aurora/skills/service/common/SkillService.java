package pl.lodz.p.aurora.skills.service.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.skills.domain.dto.SkillSearchDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

import java.util.List;

public interface SkillService {

    List<Skill> findAll();

    Page<Skill> findAllByPage(Pageable pageable);

    Skill findById(Long skillId);

    Page<Skill> search(SkillSearchDto criteria, Pageable pageable);
}
