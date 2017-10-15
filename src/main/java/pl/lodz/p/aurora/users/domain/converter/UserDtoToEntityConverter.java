package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;

@Component
public class UserDtoToEntityConverter implements Converter<UserDto, User> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserDtoToEntityConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User convert(UserDto user) {
        User convertedUser = modelMapper.map(user, User.class);

        return convertedUser;
    }
}
