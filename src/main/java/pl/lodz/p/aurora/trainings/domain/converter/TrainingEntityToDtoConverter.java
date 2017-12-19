package pl.lodz.p.aurora.trainings.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingDto;
import pl.lodz.p.aurora.trainings.domain.entity.Training;

public class TrainingEntityToDtoConverter extends BaseConverter implements Converter<Training, TrainingDto> {
    @Override
    public TrainingDto convert(Training entity) {
        return mapper.map(entity, TrainingDto.class);
    }
}
