package pl.lodz.p.aurora.mus.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mus.web.dto.DutyDto;
import pl.lodz.p.aurora.mus.domain.entity.Duty;

public class DutyEntityToDtoConverter implements Converter<Duty, DutyDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public DutyDto convert(Duty duty) {
        return mapper.map(duty, DutyDto.class);
    }
}