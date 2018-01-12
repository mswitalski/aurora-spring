package pl.lodz.p.aurora.mus.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mus.web.dto.DutyBasicDto;
import pl.lodz.p.aurora.mus.domain.entity.Duty;

public class DutyBasicDtoConverter implements Converter<Duty, DutyBasicDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public DutyBasicDto convert(Duty duty) {
        return mapper.map(duty, DutyBasicDto.class);
    }
}
