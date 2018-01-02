package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

public class DutyDtoToEntityConverter implements Converter<DutyDto, Duty> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Duty convert(DutyDto duty) {
        return mapper.map(duty, Duty.class);
    }
}
