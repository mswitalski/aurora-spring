package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.DutyBasicDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

@Component
public class DutyBasicDtoConverter implements Converter<Duty, DutyBasicDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public DutyBasicDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DutyBasicDto convert(Duty duty) {
        return modelMapper.map(duty, DutyBasicDto.class);
    }
}
