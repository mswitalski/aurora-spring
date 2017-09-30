package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.aurora.users.domain.entity.User;

/**
 * Repository interface for users entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByOrderBySurnameAscNameAsc(Pageable pageable);
    User findDistinctByUsername(String username);
    User findDistinctByEmail(String email);
}
