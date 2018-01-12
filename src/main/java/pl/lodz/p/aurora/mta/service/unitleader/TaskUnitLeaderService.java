package pl.lodz.p.aurora.mta.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.mta.web.dto.StatisticsDto;
import pl.lodz.p.aurora.mta.domain.entity.Task;

import java.util.List;

public interface TaskUnitLeaderService {

    List<Task> findUsersAllUndoneTasks(Long userId);

    Page<Task> findUsersDoneTasks(Long userId, Pageable pageable);

    StatisticsDto calculateUsersStatistics(Long userId);
}
