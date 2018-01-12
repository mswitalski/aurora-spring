package pl.lodz.p.aurora.mme.web.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mme.web.dto.FeedbackDto;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;

public class FeedbackDtoToEntityConverter implements Converter<FeedbackDto, Feedback> {

    private final TypeMap<FeedbackDto, Feedback> typeMap;
    private final ModelMapper mapper = new ModelMapper();

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
