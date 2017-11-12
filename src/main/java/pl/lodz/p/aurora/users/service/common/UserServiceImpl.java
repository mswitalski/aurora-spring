package pl.lodz.p.aurora.users.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.users.domain.dto.UserSearchDto;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void delete(Long userId, String eTag) {
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        userRepository.delete(storedUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAllByOrderBySurnameAscNameAsc(pageable);
    }

    @Override
    public User findById(Long userId) {
        User storedUsed = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUsed, userId);

        return storedUsed;
    }

    @Override
    public User findByUsername(String username) {
        User storedUsed = userRepository.findDistinctByUsername(username);
        failIfNoRecordInDatabaseFound(storedUsed, username);

        return storedUsed;
    }

    @Override
    public Page<User> search(UserSearchDto critieria, Pageable pageable) {
        return this.userRepository.searchForUser(
                critieria.getUsername(),
                critieria.getName(),
                critieria.getSurname(),
                critieria.getEmail(),
                pageable
        );
    }
}
