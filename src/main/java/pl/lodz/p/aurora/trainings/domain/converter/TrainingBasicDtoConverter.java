package pl.lodz.p.aurora.trainings.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingBasicDto;
import pl.lodz.p.aurora.trainings.domain.entity.Training;

public class TrainingBasicDtoConverter extends BaseConverter implements Converter<Training, TrainingBasicDto> {
    @Override
    public TrainingBasicDto convert(Training entity) {
        return mapper.map(entity, TrainingBasicDto.class);
    }
}
