package pl.lodz.p.aurora.trainings.domain.converter;

import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingDto;
import pl.lodz.p.aurora.trainings.domain.entity.Training;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class TrainingDtoToEntityConverter extends BaseConverter implements Converter<TrainingDto, Training> {

    private final TypeMap<TrainingDto, Training> typeMap;

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
