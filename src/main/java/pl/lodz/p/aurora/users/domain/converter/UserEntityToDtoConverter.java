package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.DutyBasicDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserEntityToDtoConverter implements Converter<User, UserDto> {

    private final ModelMapper modelMapper;
    private final DutyBasicDtoConverter dutyConverter;

    @Autowired
    public UserEntityToDtoConverter(ModelMapper modelMapper, DutyBasicDtoConverter dutyConverter) {
        this.modelMapper = modelMapper;
        this.dutyConverter = dutyConverter;
    }

    @Override
    public UserDto convert(User user) {
        UserDto convertedUser = modelMapper.map(user, UserDto.class);
        convertedUser.setDuties(prepareDuties(user));

        return convertedUser;
    }

    private Set<DutyBasicDto> prepareDuties(User user) {
        return user.getDuties().stream().map(dutyConverter::convert).collect(Collectors.toSet());
    }
}
