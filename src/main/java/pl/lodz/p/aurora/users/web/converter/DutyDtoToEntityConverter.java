package pl.lodz.p.aurora.users.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.users.web.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

public class DutyDtoToEntityConverter implements Converter<DutyDto, Duty> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Duty convert(DutyDto dto) {
        return mapper.map(dto, Duty.class);
    }
}
