package pl.lodz.p.aurora.mentors.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

public class MentorEntityToDtoConverter extends BaseConverter implements Converter<Mentor, MentorDto> {
    @Override
    public MentorDto convert(Mentor mentor) {
        return mapper.map(mentor, MentorDto.class);
    }
}
