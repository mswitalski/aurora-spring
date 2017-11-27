package pl.lodz.p.aurora.users.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;
import pl.lodz.p.aurora.users.domain.entity.User;

public class UserBasicDtoConverter extends BaseConverter implements Converter<User, UserBasicDto> {

    @Override
    public UserBasicDto convert(User user) {
        return mapper.map(user, UserBasicDto.class);
    }
}
