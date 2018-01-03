package pl.lodz.p.aurora.users.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.domain.dto.UserSearchDto;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT_LEADER')")
    public Page<User> findAllByPage(Pageable pageable) {
        return repository.findAllByOrderBySurnameAscNameAsc(pageable);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_UNIT_LEADER')")
    public User findById(Long userId) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, userId);

        return storedUser;
    }

    /**
     * Find user by given username.
     * *IMPORTANT* Do not put any PreAuthorize/Secured constraints on this method!
     *
     * @param username Username of the user to be found
     * @return Found user
     */
    @Override
    public User findByUsername(String username) {
        User storedUser = repository.findDistinctByUsername(username);

        failIfNoRecordInDatabaseFound(storedUser, username);

        return storedUser;
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT_LEADER')")
    public Page<User> search(UserSearchDto critieria, Pageable pageable) {
        return this.repository.search(
                critieria.getUsername(),
                critieria.getName(),
                critieria.getSurname(),
                critieria.getEmail(),
                pageable
        );
    }
}
