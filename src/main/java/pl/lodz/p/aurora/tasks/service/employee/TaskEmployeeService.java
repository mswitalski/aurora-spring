package pl.lodz.p.aurora.tasks.service.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.tasks.web.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

public interface TaskEmployeeService {

    StatisticsDto calculateUserStatistics(User employee);

    Task create(Task task, User employee);

    Task findById(Long taskId, User employee);

    List<Task> findUsersAllUndoneTasks(User employee);

    Page<Task> findUsersDoneTasks(User employee, Pageable pageable);

    void delete(Long taskId, User employee, String eTag);

    void update(Long taskId, Task task, String eTag, User employee);
}
