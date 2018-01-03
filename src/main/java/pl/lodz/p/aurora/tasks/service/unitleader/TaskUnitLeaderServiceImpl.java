package pl.lodz.p.aurora.tasks.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.tasks.domain.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;
import pl.lodz.p.aurora.tasks.domain.repository.TaskRepository;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.common.UserService;

import java.util.List;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mtaTransactionManager")
public class TaskUnitLeaderServiceImpl extends BaseService implements TaskUnitLeaderService {

    private final TaskRepository repository;
    private final UserService userService;

    @Autowired
    public TaskUnitLeaderServiceImpl(TaskRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public List<Task> findUsersAllUndoneTasks(Long userId) {
        User storedUser = userService.findById(userId);

        return repository.findAllUsersUndoneTasks(storedUser);
    }

    @Override
    public Page<Task> findUsersDoneTasks(Long userId, Pageable pageable) {
        User storedUser = userService.findById(userId);

        return repository.findUsersDoneTasks(storedUser, pageable);
    }

    @Override
    public StatisticsDto calculateUsersStatistics(Long userId) {
        User storedUser = userService.findById(userId);
        List<Task> tasks = repository.findAllByUser(storedUser);
        StatisticsDto stats = new StatisticsDto();
        tasks.forEach(stats::processTask);

        return stats;
    }
}
