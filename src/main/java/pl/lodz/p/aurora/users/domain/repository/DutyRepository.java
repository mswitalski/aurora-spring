package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.users.domain.entity.Duty;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ)
public interface DutyRepository extends JpaRepository<Duty, Long> {

    Page<Duty> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
