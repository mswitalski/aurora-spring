package pl.lodz.p.aurora.users.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class ProfileServiceImpl extends BaseService implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Autowired
    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoderProvider passwordEncoderProvider) {
        this.userRepository = userRepository;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    @Override
    public void update(String eTag, User user) {
        User storedUser = userRepository.findDistinctByUsername(user.getUsername());

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);

        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setGoals(user.getGoals());
        save(storedUser, userRepository);
    }

    @Override
    public boolean updatePassword(String username, String newPassword, String oldPassword, String eTag) {
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
