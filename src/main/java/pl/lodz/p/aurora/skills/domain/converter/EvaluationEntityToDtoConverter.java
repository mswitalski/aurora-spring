package pl.lodz.p.aurora.skills.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.skills.domain.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;

public class EvaluationEntityToDtoConverter extends BaseConverter implements Converter<Evaluation, EvaluationDto> {

    @Override
    public EvaluationDto convert(Evaluation evaluation) {
        return mapper.map(evaluation, EvaluationDto.class);
    }
}