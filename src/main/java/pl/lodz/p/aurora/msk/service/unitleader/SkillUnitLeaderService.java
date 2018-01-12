package pl.lodz.p.aurora.msk.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.msk.web.dto.SkillSearchDto;
import pl.lodz.p.aurora.msk.domain.entity.Skill;

public interface SkillUnitLeaderService {

    Skill create(Skill skill);

    void delete(Long skillId, String eTag);

    Page<Skill> findAllByPage(Pageable pageable);

    Page<Skill> search(SkillSearchDto criteria, Pageable pageable);

    void update(Long skillId, Skill skill, String eTag);
}
