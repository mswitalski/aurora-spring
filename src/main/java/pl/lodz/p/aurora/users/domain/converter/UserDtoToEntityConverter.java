package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyBasicDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDtoToEntityConverter extends BaseConverter implements Converter<UserDto, User> {

    private final TypeMap<UserDto, User> typeMap;

    public UserDtoToEntityConverter() {
        typeMap = mapper.createTypeMap(UserDto.class, User.class).
                addMappings(mapper -> mapper.skip(User::setDuties));
    }

    @Override
    public User convert(UserDto user) {
        User convertedUser = typeMap.map(user);
        convertedUser.setDuties(convertDuties(user.getDuties()));

        return convertedUser;
    }

    private Set<Duty> convertDuties(Set<DutyBasicDto> basicDuties) {
        return basicDuties.stream().map(duty -> mapper.map(duty, Duty.class)).collect(Collectors.toSet());
    }
}
