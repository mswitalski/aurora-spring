package pl.lodz.p.aurora.tasks.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.tasks.domain.entity.Task;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mtaTransactionManager")
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "select t from Task t where t.doneDate = null and t.user = ?1 order by t.deadlineDate asc")
    List<Task> findAllUsersUndoneTasks(User user);

    @Query(value = "select t from Task t where t.doneDate <> null and t.user = ?1 order by t.doneDate desc")
    Page<Task> findUsersDoneTasks(User user, Pageable pageable);

    List<Task> findAllByUser(User user);
}
