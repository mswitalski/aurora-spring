package pl.lodz.p.aurora.tasks.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.tasks.domain.dto.TaskDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;

public class TaskEntityToDtoConverter extends BaseConverter implements Converter<Task, TaskDto> {

    @Override
    public TaskDto convert(Task task) {
        return mapper.map(task, TaskDto.class);
    }
}
