package pl.lodz.p.aurora.skills.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.skills.domain.dto.SkillDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

public class SkillEntityToDtoConverter extends BaseConverter implements Converter<Skill, SkillDto> {

    @Override
    public SkillDto convert(Skill skill) {
        return mapper.map(skill, SkillDto.class);
    }
}