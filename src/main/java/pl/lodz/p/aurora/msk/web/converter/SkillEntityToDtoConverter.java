package pl.lodz.p.aurora.msk.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.msk.web.dto.SkillDto;
import pl.lodz.p.aurora.msk.domain.entity.Skill;

public class SkillEntityToDtoConverter implements Converter<Skill, SkillDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public SkillDto convert(Skill skill) {
        return mapper.map(skill, SkillDto.class);
    }
}