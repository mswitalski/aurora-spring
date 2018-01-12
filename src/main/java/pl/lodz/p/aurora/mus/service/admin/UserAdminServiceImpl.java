package pl.lodz.p.aurora.mus.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.domain.repository.UserRepository;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
public class UserAdminServiceImpl extends BaseService implements UserAdminService {

    private final UserRepository repository;
    private final PasswordEncoderProvider encoderProvider;

    @Value("${aurora.default.role.admin.name}")
    private String defaultAdminRoleName;

    @Autowired
    public UserAdminServiceImpl(UserRepository repository, PasswordEncoderProvider encoderProvider) {
        this.repository = repository;
        this.encoderProvider = encoderProvider;
    }

    @Override
    public User create(User user) {
        user.setPassword(encoderProvider.getEncoder().encode(user.getPassword()));

        return save(user, repository);
    }

    @Override
    public void delete(Long userId, String eTag) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        repository.delete(storedUser);
    }

    @Override
    public void update(Long userId, User user, String eTag) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setGoals(user.getGoals());
        storedUser.setEmail(user.getEmail());
        storedUser.setPosition(user.getPosition());
        storedUser.setEnabled(user.isEnabled());
        storedUser.setRoles(user.getRoles());
        save(storedUser, repository);
    }

    @Override
    public void updatePassword(Long userId, String newPassword, String eTag) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setPassword(encoderProvider.getEncoder().encode(newPassword));
        save(storedUser, repository);
    }
}