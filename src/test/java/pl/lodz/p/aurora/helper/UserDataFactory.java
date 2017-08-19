package pl.lodz.p.aurora.helper;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

/**
 * Utility class providing test data for features related to users.
 */
@Component
public class UserDataFactory extends EntityDataFactory<User> {

    @Value("${aurora.test.default.password}")
    private String defaultPassword;

    private RandomStringGenerator randomGenerator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();

    @Autowired
    public void setRepository(UserRepository userRepository) {
        repository = userRepository;
    }

    /**
     * Provide a single dummy users entity without saving to database.
     *
     * @return Dummy users entity saved to database
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
}
