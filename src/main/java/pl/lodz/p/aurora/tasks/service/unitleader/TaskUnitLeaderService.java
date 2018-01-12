package pl.lodz.p.aurora.tasks.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.tasks.web.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;

import java.util.List;

public interface TaskUnitLeaderService {

    List<Task> findUsersAllUndoneTasks(Long userId);

    Page<Task> findUsersDoneTasks(Long userId, Pageable pageable);

    StatisticsDto calculateUsersStatistics(Long userId);
}
