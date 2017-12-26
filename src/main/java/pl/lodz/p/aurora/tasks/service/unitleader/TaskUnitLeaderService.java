package pl.lodz.p.aurora.tasks.service.unitleader;

import pl.lodz.p.aurora.tasks.domain.dto.StatisticsDto;
import pl.lodz.p.aurora.tasks.domain.entity.Task;

import java.util.List;

public interface TaskUnitLeaderService {

    List<Task> findUsersAllUndoneTasks(Long userId);

    List<Task> findUsersDoneTaskFromLastWeek(Long userId);

    StatisticsDto calculateUsersStatistics(Long userId);
}
