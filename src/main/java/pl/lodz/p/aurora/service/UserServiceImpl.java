package pl.lodz.p.aurora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.domain.entity.User;
import pl.lodz.p.aurora.domain.repository.UserRepository;

import java.util.List;

/**
 * Service class used for processing user account data.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
     * Find user saved in data source by username.
     *
     * @return User with given username
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findDistinctByUsername(username);
    }
}
