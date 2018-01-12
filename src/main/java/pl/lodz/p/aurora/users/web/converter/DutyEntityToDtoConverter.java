package pl.lodz.p.aurora.users.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.users.web.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

public class DutyEntityToDtoConverter implements Converter<Duty, DutyDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public DutyDto convert(Duty duty) {
        return mapper.map(duty, DutyDto.class);
    }
}