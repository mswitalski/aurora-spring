package pl.lodz.p.aurora.tasks.domain.converter;

import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.tasks.domain.dto.TaskDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;

public class TaskDtoToEntityConverter extends BaseConverter implements Converter<TaskDto, Task> {

    @Override
    public Task convert(TaskDto dto) {
        return mapper.map(dto, Task.class);
    }
}
