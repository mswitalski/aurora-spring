package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.aurora.users.domain.entity.Role;

import java.util.List;

/**
 * Repository interface for role entity.
 */
public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> findAll();
    Role findByName(String name);
}
