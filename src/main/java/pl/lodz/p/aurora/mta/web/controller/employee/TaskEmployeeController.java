package pl.lodz.p.aurora.mta.web.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.mta.web.converter.TaskDtoToEntityConverter;
import pl.lodz.p.aurora.mta.web.converter.TaskEntityToDtoConverter;
import pl.lodz.p.aurora.mta.web.dto.StatisticsDto;
import pl.lodz.p.aurora.mta.web.dto.TaskDto;
import pl.lodz.p.aurora.mta.domain.entity.Task;
import pl.lodz.p.aurora.mta.service.employee.TaskEmployeeService;
import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class TaskEmployeeController extends BaseController {

    private final TaskEmployeeService service;
    private final TaskDtoToEntityConverter dtoToEntityConverter = new TaskDtoToEntityConverter();
    private final TaskEntityToDtoConverter entityToDtoConverter = new TaskEntityToDtoConverter();

    @Autowired
    public TaskEmployeeController(TaskEmployeeService service) {
        this.service = service;
    }

    @GetMapping(value = "tasks/{taskId:[\\d]+}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long taskId, @AuthenticationPrincipal User activeUser) {
        return respondWithETag(service.findById(taskId, activeUser), entityToDtoConverter);
    }

    @GetMapping(value = "users/me/tasks/undone/")
    public ResponseEntity<List<TaskDto>> findUsersAllUndoneTasks(@AuthenticationPrincipal User activeUser) {
        return ResponseEntity.ok(this.service.findUsersAllUndoneTasks(activeUser)
                .stream().map(entityToDtoConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "users/me/tasks/done/")
    public ResponseEntity<Page<TaskDto>> findUsersDoneTaskFromLastWeek(Pageable pageable, @AuthenticationPrincipal User activeUser) {
        return ResponseEntity.ok(this.service.findUsersDoneTasks(activeUser, pageable).map(entityToDtoConverter));
    }

    @GetMapping(value = "users/me/tasks/statistics")
    public ResponseEntity<StatisticsDto> calculateUsersStatistics(@AuthenticationPrincipal User activeUser) {
        return ResponseEntity.ok(this.service.calculateUserStatistics(activeUser));
    }

    @PostMapping(value = "tasks/")
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto formData, @AuthenticationPrincipal User activeUser) {
        Task receivedTask = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(service.create(receivedTask, activeUser)));
    }

    @DeleteMapping(value = "tasks/{taskId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long taskId, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.delete(taskId, activeUser, eTag);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "tasks/{taskId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long taskId, @RequestBody TaskDto task, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.update(taskId, dtoToEntityConverter.convert(task), sanitizeReceivedETag(eTag), activeUser);

        return ResponseEntity.noContent().build();
    }
}
