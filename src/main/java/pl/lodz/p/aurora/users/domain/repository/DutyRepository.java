package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.users.domain.entity.Duty;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
public interface DutyRepository extends JpaRepository<Duty, Long> {

    Page<Duty> findAllByOrderByNameAsc(Pageable pageable);

    @Query(value = "select d from Duty d where lower(d.name) like concat('%', lower(?1), '%') order by d.name asc")
    Page<Duty> search(String name, Pageable pageable);
}
