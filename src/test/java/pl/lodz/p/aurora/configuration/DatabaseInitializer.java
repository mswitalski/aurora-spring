package pl.lodz.p.aurora.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.repository.RoleRepository;

import javax.annotation.PostConstruct;

/**
 * Utility class for test database initialization.
 */
@Component
public class DatabaseInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        initializeRoles();
    }

    /**
     * Most basic roles must be defined. Without them the application won't be able to work properly.
     */
    private void initializeRoles() {
        Role employeeRole = new Role("EMPLOYEE");
        Role unitLeaderRole = new Role("UNIT_LEADER");
        Role adminRole = new Role("ADMIN");
        roleRepository.saveAndFlush(employeeRole);
        roleRepository.saveAndFlush(unitLeaderRole);
        roleRepository.saveAndFlush(adminRole);
    }
}
