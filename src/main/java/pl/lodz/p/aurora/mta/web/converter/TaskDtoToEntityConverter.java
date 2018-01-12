package pl.lodz.p.aurora.mta.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mta.web.dto.TaskDto;
import pl.lodz.p.aurora.mta.domain.entity.Task;

public class TaskDtoToEntityConverter implements Converter<TaskDto, Task> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Task convert(TaskDto dto) {
        return mapper.map(dto, Task.class);
    }
}
