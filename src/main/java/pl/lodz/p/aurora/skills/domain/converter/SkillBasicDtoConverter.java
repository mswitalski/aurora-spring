package pl.lodz.p.aurora.skills.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.skills.domain.dto.SkillBasicDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

public class SkillBasicDtoConverter extends BaseConverter implements Converter<Skill, SkillBasicDto> {

    @Override
    public SkillBasicDto convert(Skill skill) {
        return mapper.map(skill, SkillBasicDto.class);
    }
}