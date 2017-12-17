package pl.lodz.p.aurora.mentors.domain.converter;

import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.mentors.domain.dto.FeedbackDto;
import pl.lodz.p.aurora.mentors.domain.entity.Feedback;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

public class FeedbackDtoToEntityConverter extends BaseConverter implements Converter<FeedbackDto, Feedback> {

    private final TypeMap<FeedbackDto, Feedback> typeMap;

    public FeedbackDtoToEntityConverter() {
        typeMap = mapper.createTypeMap(FeedbackDto.class, Feedback.class)
                .addMappings(mapper -> mapper.skip(Feedback::setMentor))
                .addMappings(mapper -> mapper.skip(Feedback::setUser));
    }

    @Override
    public Feedback convert(FeedbackDto dto) {
        Feedback convertedFeedback = typeMap.map(dto);
        convertedFeedback.setMentor(mapper.map(dto.getMentor(), Mentor.class));

        return convertedFeedback;
    }
}
