package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.common.exception.OutdatedEntityModificationException;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.users.domain.dto.UserSearchDto;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import java.util.Collections;

/**
 * Service class used for processing users account data.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoderProvider passwordEncoderProvider;
    @Value("${aurora.default.role.employee.name}")
    private String defaultEmployeeRoleName;
    @Value("${aurora.default.role.admin.name}")
    private String defaultAdminRoleName;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoderProvider passwordEncoderProvider) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    /**
     * Save given user to data source and return managed entity with administrator privileges.
     *
     * @param user User object that we want to save to data source
     * @return Managed users entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     * @throws InvalidEntityStateException        when entity has invalid state in spite of previously DTO validation
     */
    @Override
    public User createAsAdmin(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));

        return save(user, userRepository);
    }

    /**
     * Save given user to data source and return managed entity with unit leader privileges.
     *
     * @param user User object that we want to save to data source
     * @return Managed users entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     * @throws InvalidEntityStateException        when entity has invalid state in spite of previously DTO validation
     */
    @Override
    public User createAsUnitLeader(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));
        Role employeeRole = roleService.findByName(defaultEmployeeRoleName);

        // Roles received from the client must be completely disregarded
        user.setRoles(Collections.singleton(employeeRole));

        return save(user, userRepository);
    }

    /**
     * Find all users saved in data source.
     *
     * @return List of all users saved in data source
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAllByOrderBySurnameAscNameAsc(pageable);
    }

    /**
     * Find users saved in data source by username.
     *
     * @return User with given username
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public User findByUsername(String username) {
        User storedUsed = userRepository.findDistinctByUsername(username);
        failIfNoRecordInDatabaseFound(storedUsed, username);

        return storedUsed;
    }

    /**
     * Modify existing other user account with provided data including position and whether it's enabled or not.
     *
     * @param eTag ETag value received from client
     * @param user Object holding modified user account data
     * @return Entity with modified data that was saved to data source
     * @throws InvalidResourceRequestedException   when client requested non-existing record from data source
     * @throws OutdatedEntityModificationException when client tried to perform an update with outdated data
     * @throws UniqueConstraintViolationException  when provided entity violates unique constraints
     */
    @Override
    public void updateOtherAccount(String eTag, User user) {
        User storedUser = update(eTag, user);
        storedUser.setEmail(user.getEmail());
        storedUser.setPosition(user.getPosition());
        storedUser.setEnabled(user.isEnabled());
        storedUser.setRoles(user.getRoles());
        save(storedUser, userRepository);
    }

    /**
     * Modify any user account with provided data.
     *
     * @param eTag ETag value received from client
     * @param user Object holding modified user account data
     * @return Entity with modified data that was saved to data source
     * @throws InvalidResourceRequestedException   when client requested non-existing record from data source
     * @throws OutdatedEntityModificationException when client tried to perform an update with outdated data
     */
    private User update(String eTag, User user) {
        User storedUser = userRepository.findDistinctByUsername(user.getUsername());

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);

        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setGoals(user.getGoals());

        return storedUser;
    }

    /**
     * Modify own user account with provided data.
     *
     * @param eTag ETag value received from client
     * @param user Object holding modified user account data
     * @return Entity with modified data that was saved to data source
     * @throws InvalidResourceRequestedException   when client requested non-existing record from data source
     * @throws OutdatedEntityModificationException when client tried to perform an update with outdated data
     */
    @Override
    public void updateOwnAccount(String eTag, User user) {
        save(update(eTag, user), userRepository);
    }

    @Override
    public void updatePasswordAsAdmin(Long userId, String newPassword, String eTag) {
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setPassword(passwordEncoderProvider.getEncoder().encode(newPassword));
        userRepository.saveAndFlush(storedUser);
    }

    @Override
    public boolean updateOwnPassword(String username, String newPassword, String oldPassword, String eTag) {
        User loggedUser = userRepository.findDistinctByUsername(username);
        failIfNoRecordInDatabaseFound(loggedUser, username);
        failIfEncounteredOutdatedEntity(eTag, loggedUser);

        if (!passwordEncoderProvider.getEncoder().matches(oldPassword, loggedUser.getPassword())) {
            return false;
        }

        loggedUser.setPassword(passwordEncoderProvider.getEncoder().encode(newPassword));
        userRepository.saveAndFlush(loggedUser);

        return true;
    }

    @Override
    public void delete(Long userId, String eTag) {
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        userRepository.delete(storedUser);
    }

    @Override
    public Page<User> searchForUsers(UserSearchDto critieria, Pageable pageable) {
        return this.userRepository.searchForUser(
                critieria.getUsername(),
                critieria.getName(),
                critieria.getSurname(),
                critieria.getEmail(),
                pageable);
    }
}
