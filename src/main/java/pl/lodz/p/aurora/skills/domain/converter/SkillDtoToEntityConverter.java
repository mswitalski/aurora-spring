package pl.lodz.p.aurora.skills.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.skills.domain.dto.SkillDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

public class SkillDtoToEntityConverter extends BaseConverter implements Converter<SkillDto, Skill> {

    @Override
    public Skill convert(SkillDto skill) {
        return mapper.map(skill, Skill.class);
    }
}