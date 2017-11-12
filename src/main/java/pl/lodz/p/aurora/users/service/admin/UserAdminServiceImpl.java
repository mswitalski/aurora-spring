package pl.lodz.p.aurora.users.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class UserAdminServiceImpl extends BaseService implements UserAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Value("${aurora.default.role.admin.name}")
    private String defaultAdminRoleName;

    @Autowired
    public UserAdminServiceImpl(UserRepository userRepository, PasswordEncoderProvider passwordEncoderProvider) {
        this.userRepository = userRepository;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    @Override
    public User create(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));

        return save(user, userRepository);
    }

    @Override
    public void update(String eTag, User user) {
        User storedUser = userRepository.findDistinctByUsername(user.getUsername());

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);

        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setGoals(user.getGoals());
        storedUser.setEmail(user.getEmail());
        storedUser.setPosition(user.getPosition());
        storedUser.setEnabled(user.isEnabled());
        storedUser.setRoles(user.getRoles());
        save(storedUser, userRepository);
    }

    @Override
    public void updatePassword(Long userId, String newPassword, String eTag) {
        User storedUser = userRepository.findOne(userId);
        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setPassword(passwordEncoderProvider.getEncoder().encode(newPassword));
        userRepository.saveAndFlush(storedUser);
    }
}