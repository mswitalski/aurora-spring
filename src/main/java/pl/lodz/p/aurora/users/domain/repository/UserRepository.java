package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

/**
 * Repository interface for users entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();
    User findDistinctByUsername(String username);
    User findDistinctByEmail(String email);
}
