package pl.lodz.p.aurora.mus.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.mus.domain.entity.Role;

/**
 * Repository interface for role entity.
 */
@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "musTransactionManager")
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByName(String name);
}
