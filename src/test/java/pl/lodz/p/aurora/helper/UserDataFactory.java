package pl.lodz.p.aurora.helper;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

/**
 * Utility class providing test data for features related to users.
 */
@Component
public class UserDataFactory extends EntityDataFactory<User> {

    @Value("${aurora.test.default.password}")
    private String defaultPassword;
    private RoleDataFactory roleDataFactory;
    private RandomStringGenerator randomGenerator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();

    @Autowired
    public void setRepository(UserRepository userRepository, RoleDataFactory roleDataFactory) {
        repository = userRepository;
        this.roleDataFactory = roleDataFactory;
    }

    /**
     * Provide a single dummy user entity without saving to database.
     *
     * @return Dummy user entity
     */
    @Override
    public User createSingle() {
        String randomString = randomGenerator.generate(10);
        String randomEmail = randomString + '@' + randomString + ".com";

        return new User(
                randomString,
                defaultPassword,
                randomEmail,
                randomString,
                randomString,
                randomString,
                randomString,
                true
        );
    }

    /**
     * Provide a single dummy user entity with assigned random (saved to db) role to it. User is not saved to database.
     *
     * @return Dummy user entity with assigned random (saved to db) role
     */
    public User createSingleWithRandomRole() {
        User createdUser = createSingle();
        Role createdRole = roleDataFactory.createAndSaveSingle();
        createdUser.assignRole(createdRole);

        return createdUser;
    }
}
