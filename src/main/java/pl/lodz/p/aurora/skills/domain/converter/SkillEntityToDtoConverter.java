package pl.lodz.p.aurora.skills.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.skills.domain.dto.SkillDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

@Component
public class SkillEntityToDtoConverter implements Converter<Skill, SkillDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public SkillEntityToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SkillDto convert(Skill skill) {
        return modelMapper.map(skill, SkillDto.class);
    }
}