package pl.lodz.p.aurora.mtr.web.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mtr.web.dto.TrainingDto;
import pl.lodz.p.aurora.mtr.domain.entity.Training;
import pl.lodz.p.aurora.mus.web.dto.UserBasicDto;
import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class TrainingDtoToEntityConverter implements Converter<TrainingDto, Training> {

    private final TypeMap<TrainingDto, Training> typeMap;
    private final ModelMapper mapper = new ModelMapper();

    public TrainingDtoToEntityConverter() {
        typeMap = mapper.createTypeMap(TrainingDto.class, Training.class)
                .addMappings(mapper -> mapper.skip(Training::setUsers));
    }

    @Override
    public Training convert(TrainingDto dto) {
        Training convertedTrainings = typeMap.map(dto);

        if (dto.getUsers() != null) {
            convertedTrainings.setUsers(convertUsers(dto.getUsers()));
        }

        return convertedTrainings;
    }

    private Set<User> convertUsers(Set<UserBasicDto> dtos) {
        return dtos.stream().map(dto -> mapper.map(dto, User.class)).collect(Collectors.toSet());
    }
}
