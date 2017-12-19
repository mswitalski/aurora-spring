package pl.lodz.p.aurora.trainings.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.trainings.domain.entity.Training;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query(value = "select t from Training t where t.startDateTime >= current_timestamp and ?1 member of t.users order by t.startDateTime asc")
    Page<Training> findAllPlannedByUser(User user, Pageable pageable);

    @Query(value = "select t from Training t where t.startDateTime < current_timestamp and ?1 member of t.users order by t.endDateTime desc")
    Page<Training> findAllFinishedByUser(User user, Pageable pageable);

    List<Training> findAllByUsersContains(User user);
}
