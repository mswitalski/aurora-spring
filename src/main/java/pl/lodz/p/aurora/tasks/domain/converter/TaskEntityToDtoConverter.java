package pl.lodz.p.aurora.tasks.domain.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.tasks.domain.dto.TaskDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;

public class TaskEntityToDtoConverter implements Converter<Task, TaskDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public TaskDto convert(Task task) {
        return mapper.map(task, TaskDto.class);
    }
}
