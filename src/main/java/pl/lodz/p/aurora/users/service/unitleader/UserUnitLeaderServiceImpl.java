package pl.lodz.p.aurora.users.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;
import pl.lodz.p.aurora.users.service.common.RoleService;

import java.util.Collections;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class UserUnitLeaderServiceImpl extends BaseService implements UserUnitLeaderService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Value("${aurora.default.role.employee.name}")
    private String defaultEmployeeRoleName;

    @Autowired
    public UserUnitLeaderServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoderProvider passwordEncoderProvider) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    @Override
    public User create(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));
        Role employeeRole = roleService.findByName(defaultEmployeeRoleName);

        // Roles received from the client must be completely disregarded
        user.setRoles(Collections.singleton(employeeRole));

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
        save(storedUser, userRepository);
    }
}
