package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

@Component
public class DutyDtoToEntityConverter implements Converter<DutyDto, Duty> {

    private final ModelMapper modelMapper;

    @Autowired
    public DutyDtoToEntityConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Duty convert(DutyDto duty) {
        return modelMapper.map(duty, Duty.class);
    }
}
