package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.aurora.common.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.common.exception.OutdatedEntityModificationException;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.common.util.EntityVersionTransformer;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class used for processing users account data.
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Value("${aurora.default.role.name}")
    private String defaultEmployeeRoleName;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, EntityVersionTransformer transformer) {
        super(transformer);
        this.userRepository = userRepository;
        this.roleService = roleService;
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
        return create(user);
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
        Role role = roleService.findByName(defaultEmployeeRoleName);
        user.assignRole(role);

        return create(user);
    }

    /**
     * Save given user to data source and return managed entity.
     *
     * @param user User object that we want to save to data source
     * @return Managed users entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     * @throws InvalidEntityStateException when entity has invalid state in spite of previously DTO validation
     */
    private User create(User user) {
        try {
            return userRepository.saveAndFlush(user);

        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException &&
                    exception.getMessage().contains("constraint [null]")) {
                throw new InvalidEntityStateException(user, exception);
            }

            throw new UniqueConstraintViolationException(
                    exception, UserDto.class.getSimpleName(), findFieldsThatViolatedConstraints(user)
            );

        } catch (ConstraintViolationException exception) {
            throw new InvalidEntityStateException(user, exception);
        }
    }

    private Set<String> findFieldsThatViolatedConstraints(User user) {
        Set<String> nonUniqueFieldsNames = new HashSet<>();

        if (isUsernameAlreadyTaken(user.getUsername())) {
            nonUniqueFieldsNames.add("username");
        }

        if (isEmailAlreadyTaken(user.getEmail())) {
            nonUniqueFieldsNames.add("email");
        }

        return nonUniqueFieldsNames;
    }

    private boolean isUsernameAlreadyTaken(String username) {
        return userRepository.findDistinctByUsername(username) != null;
    }
    private boolean isEmailAlreadyTaken(String email) {
        return userRepository.findDistinctByEmail(email) != null;
    }

    /**
     * Find all users saved in data source.
     *
     * @return List of all users saved in data source
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Find users saved in data source by username.
     *
     * @return User with given username
     */
    @Override
    public User findByUsername(String username) {
        User storedUsed = userRepository.findDistinctByUsername(username);
        failIfNoRecordInDatabaseFound(storedUsed, username);

        return storedUsed;
    }

    /**
     * Modify existing user account with provided data.
     *
     * @param eTag ETag value received from client
     * @param user Object holding modified user account data
     * @return Entity with modified data that was saved to data source
     * @throws InvalidResourceRequestedException when client requested non-existing record from data source
     * @throws OutdatedEntityModificationException when client tried to perform an update with outdated data
     */
    @Override
    public User update(String eTag, User user) {
        User storedUser = userRepository.findDistinctByUsername(user.getUsername());

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);

        storedUser.setEmail(user.getEmail());
        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setPosition(user.getPosition());
        storedUser.setGoals(user.getGoals());

        return userRepository.saveAndFlush(storedUser);
    }

    /**
     * Enable user account.
     *
     * @param id ID of user account to be enabled
     * @param eTag ETag value received from client
     * @return Updated user entity
     */
    @Override
    public User enable(Long id, String eTag) {
        return changeUserEnabledState(id, eTag, true);
    }

    /**
     * Change account state of user chosen by id.
     *
     * @param id ID of user account to be enabled
     * @param eTag ETag value received from client
     * @param state Desired state, enabled (true) or disabled (false)
     * @return Updated user entity
     */
    private User changeUserEnabledState(Long id, String eTag, boolean state) {
        User storedUser = userRepository.findOne(id);
        failIfNoRecordInDatabaseFound(storedUser, id);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setEnabled(state);

        return userRepository.saveAndFlush(storedUser);
    }

    /**
     * Disable user account.
     *
     * @param id ID of user account to be disabled
     * @param eTag ETag value received from client
     * @return Updated user entity
     */
    @Override
    public User disable(Long id, String eTag) {
        return changeUserEnabledState(id, eTag, false);
    }
}
