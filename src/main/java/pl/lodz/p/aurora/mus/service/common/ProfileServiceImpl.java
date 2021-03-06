package pl.lodz.p.aurora.mus.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.mco.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.domain.repository.UserRepository;

@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
public class ProfileServiceImpl extends BaseService implements ProfileService {

    private final UserRepository repository;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Autowired
    public ProfileServiceImpl(UserRepository repository, PasswordEncoderProvider passwordEncoderProvider) {
        this.repository = repository;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    @Override
    public void update(String eTag, User user) {
        User storedUser = repository.findDistinctByUsername(user.getUsername());

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setGoals(user.getGoals());
        save(storedUser, repository);
    }

    @Override
    public boolean updatePassword(String username, String newPassword, String oldPassword, String eTag) {
        User loggedUser = repository.findDistinctByUsername(username);
        failIfNoRecordInDatabaseFound(loggedUser, username);
        failIfEncounteredOutdatedEntity(eTag, loggedUser);

        if (!passwordEncoderProvider.getEncoder().matches(oldPassword, loggedUser.getPassword())) {
            return false;
        }

        loggedUser.setPassword(passwordEncoderProvider.getEncoder().encode(newPassword));
        save(loggedUser, repository);

        return true;
    }
}
