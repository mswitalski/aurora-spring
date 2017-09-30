package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;

@Component
public class UserEntityToDtoConverter implements Converter<User, UserDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserEntityToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto convert(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
