package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;
import pl.lodz.p.aurora.users.domain.entity.User;

@Component
public class UserBasicDtoConverter implements Converter<User, UserBasicDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserBasicDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserBasicDto convert(User user) {
        return modelMapper.map(user, UserBasicDto.class);
    }
}
