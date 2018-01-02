package pl.lodz.p.aurora.skills.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.skills.domain.dto.SkillDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

public class SkillEntityToDtoConverter implements Converter<Skill, SkillDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public SkillDto convert(Skill skill) {
        return mapper.map(skill, SkillDto.class);
    }
}