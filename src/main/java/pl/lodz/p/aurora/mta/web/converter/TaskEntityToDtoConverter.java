package pl.lodz.p.aurora.mta.web.converter;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mta.web.dto.TaskDto;
import pl.lodz.p.aurora.mta.domain.entity.Task;

public class TaskEntityToDtoConverter implements Converter<Task, TaskDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public TaskDto convert(Task task) {
        return mapper.map(task, TaskDto.class);
    }
}
