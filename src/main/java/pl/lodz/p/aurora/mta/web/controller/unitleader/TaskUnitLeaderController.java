package pl.lodz.p.aurora.mta.web.controller.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.mta.web.converter.TaskEntityToDtoConverter;
import pl.lodz.p.aurora.mta.web.dto.StatisticsDto;
import pl.lodz.p.aurora.mta.web.dto.TaskDto;
import pl.lodz.p.aurora.mta.service.unitleader.TaskUnitLeaderService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=UNIT_LEADER")
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
    public ResponseEntity<Page<TaskDto>> findUsersDoneTaskFromLastWeek(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(this.service.findUsersDoneTasks(userId, pageable).map(converter));
    }

    @GetMapping(value = "{userId:[\\d]+}/tasks/statistics")
    public ResponseEntity<StatisticsDto> calculateUsersStatistics(@PathVariable Long userId) {
        return ResponseEntity.ok(this.service.calculateUsersStatistics(userId));
    }
}
