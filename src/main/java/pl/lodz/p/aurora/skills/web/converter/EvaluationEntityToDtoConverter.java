package pl.lodz.p.aurora.skills.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.skills.web.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;

public class EvaluationEntityToDtoConverter implements Converter<Evaluation, EvaluationDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public EvaluationDto convert(Evaluation evaluation) {
        return mapper.map(evaluation, EvaluationDto.class);
    }
}