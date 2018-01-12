package pl.lodz.p.aurora.mus.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mus.web.dto.UserBasicDto;
import pl.lodz.p.aurora.mus.domain.entity.User;

public class UserBasicDtoConverter implements Converter<User, UserBasicDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public UserBasicDto convert(User user) {
        return mapper.map(user, UserBasicDto.class);
    }
}
