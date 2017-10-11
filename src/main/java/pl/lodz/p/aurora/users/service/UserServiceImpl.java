package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import javax.validation.ConstraintViolationException;

/**
 * Service class used for processing users account data.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class UserServiceImpl extends BaseService implements UserService {

    @Value("${aurora.default.role.name}")
    private String defaultEmployeeRoleName;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoderProvider passwordEncoderProvider;

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
     * @throws InvalidEntityStateException when entity has invalid state in spite of previously DTO validation
     */
    @Override
    public User createAsAdmin(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));

        return save(user);
    }

    /**
     * Save given user to data source and return managed entity with unit leader privileges.
     *
     * @param user User object that we want to save to data source
     * @return Managed users entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     * @throws InvalidEntityStateException when entity has invalid state in spite of previously DTO validation
     */
    @Override
    public User createAsUnitLeader(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));
        Role employeeRole = roleService.findByName(defaultEmployeeRoleName);
        user.assignRole(employeeRole);

        return save(user);
    }

    /**
     * Save given user to data source and return managed entity.
     *
     * @param user User object that we want to save to data source
     * @return Managed users entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     * @throws InvalidEntityStateException when entity has invalid state in spite of previously DTO validation
     */
    private User save(User user) {
        try {
            return userRepository.saveAndFlush(user);

        } catch (DataIntegrityViolationException exception) {
            failOnUniqueConstraintViolation(exception);

        } catch (ConstraintViolationException exception) {
            throw new InvalidEntityStateException(user, exception);
        }

        return null;
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
     * @throws InvalidResourceRequestedException when client requested non-existing record from data source
     * @throws OutdatedEntityModificationException when client tried to perform an update with outdated data
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     */
    @Override
    public User updateOtherAccount(String eTag, User user) {
        User storedUser = update(eTag, user);
        storedUser.setEmail(user.getEmail());
        storedUser.setPosition(user.getPosition());
        storedUser.setEnabled(user.isEnabled());
        storedUser.setRoles(user.getRoles());

        return save(storedUser);
    }

    /**
     * Modify any user account with provided data.
     *
     * @param eTag ETag value received from client
     * @param user Object holding modified user account data
     * @return Entity with modified data that was saved to data source
     * @throws InvalidResourceRequestedException when client requested non-existing record from data source
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
     * @throws InvalidResourceRequestedException when client requested non-existing record from data source
     * @throws OutdatedEntityModificationException when client tried to perform an update with outdated data
     */
    @Override
    public User updateOwnAccount(String eTag, User user) {
        return save(update(eTag, user));
    }

    /**
     * Enable user account.
     *
     * @param userId ID of user account to be enabled
     * @param eTag ETag value received from client
     * @return Updated user entity
     */
    @Override
    public User enable(Long userId, String eTag) {
        return changeUserEnabledState(userId, eTag, true);
    }

    /**
     * Change account state of user chosen by id.
     *
     * @param userId ID of user account to be enabled
     * @param eTag ETag value received from client
     * @param state Desired state, enabled (true) or disabled (false)
     * @return Updated user entity
     */
    private User changeUserEnabledState(Long userId, String eTag, boolean state) {
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setEnabled(state);

        return userRepository.saveAndFlush(storedUser);
    }

    /**
     * Disable user account.
     *
     * @param userId ID of user account to be disabled
     * @param eTag ETag value received from client
     * @return Updated user entity
     */
    @Override
    public User disable(Long userId, String eTag) {
        return changeUserEnabledState(userId, eTag, false);
    }

    @Override
    public User updatePasswordAsAdmin(Long userId, String newPassword, String eTag) {
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setPassword(passwordEncoderProvider.getEncoder().encode(newPassword));

        return userRepository.saveAndFlush(storedUser);
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
}
