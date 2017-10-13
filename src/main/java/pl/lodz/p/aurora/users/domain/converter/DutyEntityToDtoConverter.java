package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

@Component
public class DutyEntityToDtoConverter implements Converter<Duty, DutyDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public DutyEntityToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DutyDto convert(Duty duty) {
        return modelMapper.map(duty, DutyDto.class);
    }
}