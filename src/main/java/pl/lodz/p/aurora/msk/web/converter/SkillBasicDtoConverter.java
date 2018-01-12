package pl.lodz.p.aurora.msk.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.msk.web.dto.SkillBasicDto;
import pl.lodz.p.aurora.msk.domain.entity.Skill;

public class SkillBasicDtoConverter implements Converter<Skill, SkillBasicDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public SkillBasicDto convert(Skill skill) {
        return mapper.map(skill, SkillBasicDto.class);
    }
}