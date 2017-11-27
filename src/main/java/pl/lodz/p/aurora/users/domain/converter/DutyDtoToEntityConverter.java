package pl.lodz.p.aurora.users.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

public class DutyDtoToEntityConverter extends BaseConverter implements Converter<DutyDto, Duty> {

    @Override
    public Duty convert(DutyDto duty) {
        return mapper.map(duty, Duty.class);
    }
}
