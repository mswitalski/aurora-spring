package pl.lodz.p.aurora.users.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyBasicDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

public class DutyBasicDtoConverter extends BaseConverter implements Converter<Duty, DutyBasicDto> {

    @Override
    public DutyBasicDto convert(Duty duty) {
        return mapper.map(duty, DutyBasicDto.class);
    }
}
