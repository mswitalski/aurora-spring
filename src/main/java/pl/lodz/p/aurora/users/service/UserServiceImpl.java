package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.aurora.common.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.common.exception.OutdatedEntityModificationException;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.common.util.EntityVersionTransformer;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashSet;
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
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, EntityVersionTransformer transformer, PasswordEncoderProvider passwordEncoderProvider) {
        super(transformer);
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
    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAllByOrderBySurnameAscNameAsc(pageable);
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
    public User updateAccount(String eTag, User user) {
        failOnNonUniqueEditedEmail(user);
        User storedUser = update(eTag, user);
        storedUser.setEmail(user.getEmail());
        storedUser.setPosition(user.getPosition());
        storedUser.setEnabled(user.isEnabled());

        return userRepository.saveAndFlush(storedUser);
    }

    private void failOnNonUniqueEditedEmail(User user) {
        User userFoundByEmail = userRepository.findDistinctByEmail(user.getEmail());

        if (userFoundByEmail != null && !userFoundByEmail.getUsername().equals(user.getUsername())) {
            throw new UniqueConstraintViolationException(
                    UserDto.class.getSimpleName(), new HashSet<>(Collections.singletonList("email"))
            );
        }
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
        return userRepository.saveAndFlush(update(eTag, user));
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

    /**
     * Assign chosen role to the user given by ID.
     *
     * @param userId User's ID to whom we want to assign a role
     * @param roleName Chosen role's name
     * @param eTag ETag value received from client
     * @return Updated user entity
     */
    @Override
    public User assignRole(Long userId, String roleName, String eTag) {
        Role chosenRole = roleService.findByName(roleName);
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.assignRole(chosenRole);

        return userRepository.saveAndFlush(storedUser);
    }

    /**
     * Retract chosen role from the user given by ID.
     *
     * @param userId User's ID to whom we want to assign a role
     * @param roleName Chosen role's name
     * @param eTag ETag value received from client
     * @return Updated user entity
     */
    @Override
    public User retractRole(Long userId, String roleName, String eTag) {
        Role chosenRole = roleService.findByName(roleName);
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.retractRole(chosenRole);

        return userRepository.saveAndFlush(storedUser);
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
