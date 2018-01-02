package pl.lodz.p.aurora.trainings.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingDto;
import pl.lodz.p.aurora.trainings.domain.entity.Training;

public class TrainingEntityToDtoConverter implements Converter<Training, TrainingDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public TrainingDto convert(Training entity) {
        return mapper.map(entity, TrainingDto.class);
    }
}
