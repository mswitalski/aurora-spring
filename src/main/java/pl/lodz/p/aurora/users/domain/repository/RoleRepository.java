package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.users.domain.entity.Role;

import java.util.List;

/**
 * Repository interface for role entity.
 */
@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> findAll();
    Role findByName(String name);
}
