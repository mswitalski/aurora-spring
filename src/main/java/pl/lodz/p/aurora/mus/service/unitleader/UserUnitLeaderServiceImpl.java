package pl.lodz.p.aurora.mus.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.exception.ActionForbiddenException;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.mco.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.mus.domain.entity.Duty;
import pl.lodz.p.aurora.mus.domain.entity.Role;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.domain.repository.UserRepository;
import pl.lodz.p.aurora.mus.service.common.RoleService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "musTransactionManager")
public class UserUnitLeaderServiceImpl extends BaseService implements UserUnitLeaderService {

    private final UserRepository repository;
    private final RoleService roleService;
    private final DutyUnitLeaderService dutyUnitLeaderService;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Value("${aurora.default.role.employee.name}")
    private String defaultEmployeeRoleName;

    @Autowired
    public UserUnitLeaderServiceImpl(UserRepository repository, RoleService roleService, DutyUnitLeaderService dutyService, PasswordEncoderProvider encoder) {
        this.repository = repository;
        this.roleService = roleService;
        this.dutyUnitLeaderService = dutyService;
        this.passwordEncoderProvider = encoder;
    }

    @Override
    public User create(User user) {
        Role employeeRole = roleService.findByName(defaultEmployeeRoleName);

        // Roles received from the client must be completely disregarded
        user.setRoles(Collections.singleton(employeeRole));
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));

        return save(user, repository);
    }

    @Override
    public void delete(Long userId, String eTag) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, userId);
        failIfTriedToChangeNonEmployee(storedUser);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        repository.delete(storedUser);
    }

    @Override
    public List<User> findAll() {
        return this.repository.findAll();
    }

    private void failIfTriedToChangeNonEmployee(User user) {
        Role employeeRole = roleService.findByName(defaultEmployeeRoleName);

        if (user.getRoles().size() > 1 || !user.getRoles().iterator().next().equals(employeeRole)) {
            throw new ActionForbiddenException("Unit leader tried to change non employee user: " + user);
        }
    }

    @Override
    public void update(Long userId, User user, String eTag) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfTriedToChangeNonEmployee(storedUser);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        storedUser.setName(user.getName());
        storedUser.setSurname(user.getSurname());
        storedUser.setGoals(user.getGoals());
        storedUser.setEmail(user.getEmail());
        storedUser.setPosition(user.getPosition());
        storedUser.setEnabled(user.isEnabled());
        save(storedUser, repository);
    }

    @Override
    public void updateDuties(Long userId, User user, String eTag) {
        User storedUser = repository.findOne(userId);

        failIfNoRecordInDatabaseFound(storedUser, user);
        failIfEncounteredOutdatedEntity(eTag, storedUser);
        Set<Duty> duties = user.getDuties().stream()
                .map(d -> dutyUnitLeaderService.findById(d.getId())).collect(Collectors.toSet());
        storedUser.setDuties(duties);
        save(storedUser, repository);
    }
}
