package pl.lodz.p.aurora.mentors.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.mentors.domain.dto.FeedbackDto;
import pl.lodz.p.aurora.mentors.domain.entity.Feedback;

public class FeedbackEntityToDtoConverter extends BaseConverter implements Converter<Feedback, FeedbackDto> {

    @Override
    public FeedbackDto convert(Feedback feedback) {
        return mapper.map(feedback, FeedbackDto.class);
    }
}