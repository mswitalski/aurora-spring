package pl.lodz.p.aurora.tasks.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.tasks.domain.converter.TaskEntityToDtoConverter;
import pl.lodz.p.aurora.tasks.domain.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.dto.TaskDto;
import pl.lodz.p.aurora.tasks.service.unitleader.TaskUnitLeaderService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=ANY")
@RestController
public class TaskUnitLeaderController extends BaseController {

    private final TaskUnitLeaderService service;
    private final TaskEntityToDtoConverter converter = new TaskEntityToDtoConverter();

    @Autowired
    public TaskUnitLeaderController(TaskUnitLeaderService service) {
        this.service = service;
    }

    @GetMapping(value = "{userId:[\\d]+}/tasks/undone/")
    public ResponseEntity<List<TaskDto>> findUsersAllUndoneTasks(@PathVariable Long userId) {
        return ResponseEntity.ok(this.service.findUsersAllUndoneTasks(userId)
                .stream().map(converter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "{userId:[\\d]+}/tasks/done/")
    public ResponseEntity<List<TaskDto>> findUsersDoneTaskFromLastWeek(@PathVariable Long userId) {
        return ResponseEntity.ok(this.service.findUsersDoneTaskFromLastWeek(userId)
                .stream().map(converter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "{userId:[\\d]+}/tasks/statistics")
    public ResponseEntity<StatisticsDto> calculateUsersStatistics(@PathVariable Long userId) {
        return ResponseEntity.ok(this.service.calculateUsersStatistics(userId));
    }
}
