package pl.lodz.p.aurora.skills.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.skills.domain.dto.SkillBasicDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

@Component
public class SkillBasicDtoConverter implements Converter<Skill, SkillBasicDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public SkillBasicDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SkillBasicDto convert(Skill skill) {
        return modelMapper.map(skill, SkillBasicDto.class);
    }
}