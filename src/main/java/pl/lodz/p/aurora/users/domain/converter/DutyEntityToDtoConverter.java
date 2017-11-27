package pl.lodz.p.aurora.users.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

public class DutyEntityToDtoConverter extends BaseConverter implements Converter<Duty, DutyDto> {

    @Override
    public DutyDto convert(Duty duty) {
        return mapper.map(duty, DutyDto.class);
    }
}