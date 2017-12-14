package pl.lodz.p.aurora.mentors.domain.converter;

import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.skills.domain.entity.Skill;
import pl.lodz.p.aurora.users.domain.entity.User;

public class MentorDtoToEntityConverter extends BaseConverter implements Converter<MentorDto, Mentor> {

    private final TypeMap<MentorDto, Mentor> typeMap;

    public MentorDtoToEntityConverter() {
        typeMap = mapper.createTypeMap(MentorDto.class, Mentor.class)
                .addMappings(mapper -> mapper.skip(Mentor::setSkill))
                .addMappings(mapper -> mapper.skip(Mentor::setUser));
    }

    @Override
    public Mentor convert(MentorDto dto) {
        Mentor convertedMentor = typeMap.map(dto);
        convertedMentor.setSkill(mapper.map(dto.getSkill(), Skill.class));
        convertedMentor.setUser(mapper.map(dto.getUser(), User.class));

        return convertedMentor;
    }
}
