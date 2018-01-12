package pl.lodz.p.aurora.mtr.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mtr.web.dto.TrainingBasicDto;
import pl.lodz.p.aurora.mtr.domain.entity.Training;

public class TrainingBasicDtoConverter implements Converter<Training, TrainingBasicDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public TrainingBasicDto convert(Training entity) {
        return mapper.map(entity, TrainingBasicDto.class);
    }
}
