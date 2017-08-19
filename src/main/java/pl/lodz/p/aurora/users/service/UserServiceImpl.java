package pl.lodz.p.aurora.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class used for processing users account data.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Save given users to data source and return managed entity.
     *
     * @param user User object that we want to save to data source
     * @return Managed users entity saved to data source
     * @throws UniqueConstraintViolationException when provided entity violates unique constraints
     */
    @Override
    public User create(User user) throws UniqueConstraintViolationException {
        try {
            return userRepository.saveAndFlush(user);

        } catch (DataIntegrityViolationException exception) {
            throw new UniqueConstraintViolationException(User.class.getSimpleName(), findFieldsThatViolatedConstraints(user));
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
        return userRepository.findDistinctByUsername(username);
    }

    private boolean isUsernameAlreadyTaken(String username) {
        return userRepository.findDistinctByUsername(username) != null;
    }
    private boolean isEmailAlreadyTaken(String email) {
        return userRepository.findDistinctByEmail(email) != null;
    }
}
