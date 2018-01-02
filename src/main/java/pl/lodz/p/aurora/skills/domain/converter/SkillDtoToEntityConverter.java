package pl.lodz.p.aurora.skills.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.skills.domain.dto.SkillDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

public class SkillDtoToEntityConverter implements Converter<SkillDto, Skill> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Skill convert(SkillDto skill) {
        return mapper.map(skill, Skill.class);
    }
}