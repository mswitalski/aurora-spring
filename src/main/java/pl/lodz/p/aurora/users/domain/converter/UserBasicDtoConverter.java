package pl.lodz.p.aurora.users.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;
import pl.lodz.p.aurora.users.domain.entity.User;

public class UserBasicDtoConverter implements Converter<User, UserBasicDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public UserBasicDto convert(User user) {
        return mapper.map(user, UserBasicDto.class);
    }
}
