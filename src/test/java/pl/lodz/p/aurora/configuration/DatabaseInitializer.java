package pl.lodz.p.aurora.configuration;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.configuration.security.PasswordEncoderProvider;
import pl.lodz.p.aurora.mus.domain.entity.Role;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.domain.repository.RoleRepository;
import pl.lodz.p.aurora.mus.domain.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Initialize database with data necessary for testing purposes.
 */
@Component
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${aurora.test.credential.admin.username}")
    protected String adminUsername;
    @Value("${aurora.test.credential.admin.password}")
    protected String adminPassword;
    @Value("${aurora.test.credential.unitleader.username}")
    protected String unitLeaderUsername;
    @Value("${aurora.test.credential.unitleader.password}")
    protected String unitLeaderPassword;
    @Value("${aurora.test.credential.employee.username}")
    protected String employeeUsername;
    @Value("${aurora.test.credential.employee.password}")
    protected String employeePassword;
    @Autowired
    private PasswordEncoderProvider passwordEncoder;
    private RandomStringGenerator randomGenerator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();

    @PostConstruct
    private void initDatabase() {
        // Prepare roles
        List<String> necessaryRoles = Arrays.asList("ADMIN", "UNIT_LEADER", "EMPLOYEE");
        List<Role> rolesList = roleRepository.findAll();

        necessaryRoles.forEach(role -> {
            Role newRole = new Role(role);
            rolesList.add(roleRepository.saveAndFlush(newRole));
        });

        // Prepare users
        String randomEmail = randomGenerator.generate(5) + "@doo.bee.doo";

        User adminUser = new User(
                adminUsername,
                passwordEncoder.getEncoder().encode(adminPassword),
                randomEmail + "admin",
                "John",
                "Smith",
                "Boss",
                "Make dreams come true",
                true);
        adminUser.assignRole(rolesList.stream().filter(r -> r.getName().equals("ADMIN")).findFirst().get());

        User unitLeaderUser = new User(
                unitLeaderUsername,
                passwordEncoder.getEncoder().encode(unitLeaderPassword),
                randomEmail + "unitleader",
                "Anne",
                "Brown",
                "Unit leader",
                "Deliver IoT Nova project before deadline",
                true);
        unitLeaderUser.assignRole(rolesList.stream().filter(r -> r.getName().equals("UNIT_LEADER")).findFirst().get());

        User employeeUser = new User(
                employeeUsername,
                passwordEncoder.getEncoder().encode(employeePassword),
                randomEmail + "employee",
                "Caroline",
                "Fox",
                "Employee",
                "Become more proficient as Java programmer and get promoted to Regular Programmer before the end of the year",
                true);
        employeeUser.assignRole(rolesList.stream().filter(r -> r.getName().equals("EMPLOYEE")).findFirst().get());

        userRepository.saveAndFlush(adminUser);
        userRepository.saveAndFlush(unitLeaderUser);
        userRepository.saveAndFlush(employeeUser);
    }
}
