package pl.lodz.p.aurora.mus.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mus.web.dto.DutyBasicDto;
import pl.lodz.p.aurora.mus.web.dto.UserDto;
import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserEntityToDtoConverter implements Converter<User, UserDto> {

    private final DutyBasicDtoConverter dutyConverter = new DutyBasicDtoConverter();
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public UserDto convert(User user) {
        UserDto convertedUser = mapper.map(user, UserDto.class);
        convertedUser.setDuties(prepareDuties(user));

        return convertedUser;
    }

    private Set<DutyBasicDto> prepareDuties(User user) {
        return user.getDuties().stream().map(dutyConverter::convert).collect(Collectors.toSet());
    }
}
