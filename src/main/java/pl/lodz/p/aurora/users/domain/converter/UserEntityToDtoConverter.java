package pl.lodz.p.aurora.users.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyBasicDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserEntityToDtoConverter extends BaseConverter implements Converter<User, UserDto> {

    private final DutyBasicDtoConverter dutyConverter = new DutyBasicDtoConverter();

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
