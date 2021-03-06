package pl.lodz.p.aurora.mme.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mme.web.dto.FeedbackDto;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;

public class FeedbackEntityToDtoConverter implements Converter<Feedback, FeedbackDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public FeedbackDto convert(Feedback feedback) {
        return mapper.map(feedback, FeedbackDto.class);
    }
}