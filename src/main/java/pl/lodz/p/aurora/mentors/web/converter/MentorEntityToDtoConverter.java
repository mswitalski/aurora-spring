package pl.lodz.p.aurora.mentors.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mentors.web.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

public class MentorEntityToDtoConverter implements Converter<Mentor, MentorDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public MentorDto convert(Mentor mentor) {
        return mapper.map(mentor, MentorDto.class);
    }
}
