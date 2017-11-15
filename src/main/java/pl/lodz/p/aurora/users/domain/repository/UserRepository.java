package pl.lodz.p.aurora.users.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.users.domain.entity.User;

/**
 * Repository interface for users entity.
 */
@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByOrderBySurnameAscNameAsc(Pageable pageable);

    User findDistinctByUsername(String username);

    @Query(value = "select u from User u where lower(u.username) like concat('%', lower(?1), '%') and lower(u.name) like concat('%', lower(?2), '%')" +
            " and lower(u.surname) like concat('%', lower(?3), '%') and lower(u.email) like concat('%', lower(?4), '%')" +
            " order by u.surname asc, u.name asc")
    Page<User> search(
            String username,
            String name,
            String surname,
            String email,
            Pageable pageable);
}
