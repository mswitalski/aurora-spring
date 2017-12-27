package pl.lodz.p.aurora.tasks.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.exception.ActionForbiddenException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.tasks.domain.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;
import pl.lodz.p.aurora.tasks.domain.repository.TaskRepository;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public class TaskEmployeeServiceImpl extends BaseService implements TaskEmployeeService {

    private final TaskRepository repository;

    @Autowired
    public TaskEmployeeServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public StatisticsDto calculateUserStatistics(User employee) {
        List<Task> tasks = repository.findAllByUser(employee);
        StatisticsDto stats = new StatisticsDto();
        tasks.forEach(stats::processTask);

        return stats;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public Task create(Task task, User employee) {
        task.setUser(employee);

        return save(task, repository);
    }

    @Override
    public Task findById(Long taskId, User employee) {
        Task storedTask = repository.findOne(taskId);

        failIfTriedToAccessNotOwnedTask(employee, storedTask);

        return storedTask;
    }

    private void failIfTriedToAccessNotOwnedTask(User employee, Task task) {
        if (!task.getUser().getId().equals(employee.getId())) {
            throw new ActionForbiddenException("Employee tried to change not his task: " + task);
        }
    }

    @Override
    public List<Task> findUsersAllUndoneTasks(User employee) {
        return repository.findAllUsersUndoneTasks(employee);
    }

    @Override
    public Page<Task> findUsersDoneTasks(User employee, Pageable pageable) {
        return repository.findUsersDoneTasks(employee, pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void delete(Long taskId, User employee, String eTag) {
        Task storedTask = repository.findOne(taskId);

        failIfNoRecordInDatabaseFound(storedTask, taskId);
        failIfTriedToAccessNotOwnedTask(employee, storedTask);
        failIfEncounteredOutdatedEntity(eTag, storedTask);
        repository.delete(taskId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void update(Long taskId, Task task, String eTag, User employee) {
        Task storedTask = repository.findOne(taskId);

        failIfNoRecordInDatabaseFound(storedTask, taskId);
        failIfTriedToAccessNotOwnedTask(employee, storedTask);
        failIfEncounteredOutdatedEntity(eTag, storedTask);
        storedTask.setContent(task.getContent());
        storedTask.setDeadlineDate(task.getDeadlineDate());
        storedTask.setDoneDate(task.getDoneDate());
        save(storedTask, repository);
    }
}
