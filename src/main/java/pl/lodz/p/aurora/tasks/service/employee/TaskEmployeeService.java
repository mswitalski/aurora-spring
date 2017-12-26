package pl.lodz.p.aurora.tasks.service.employee;

import pl.lodz.p.aurora.tasks.domain.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

public interface TaskEmployeeService {

    StatisticsDto calculateUserStatistics(User employee);

    Task create(Task task, User employee);

    List<Task> findUsersAllUndoneTasks(User employee);

    List<Task> findUsersDoneTaskFromLastWeek(User employee);

    void delete(Long taskId, User employee, String eTag);

    void update(Long taskId, Task task, String eTag, User employee);
}
