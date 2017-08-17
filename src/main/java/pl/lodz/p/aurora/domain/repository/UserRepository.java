package pl.lodz.p.aurora.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.aurora.domain.entity.User;

import java.util.List;

/**
 * Repository interface for user entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();
    User findDistinctByUsername(String username);
}
