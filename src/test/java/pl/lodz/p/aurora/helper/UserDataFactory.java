package pl.lodz.p.aurora.helper;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.domain.entity.User;
import pl.lodz.p.aurora.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class providing test data for features related to users.
 */
@Component
public class UserDataFactory {

    @Value("${aurora.test.default.password}")
    private String defaultPassword;

    @Autowired
    private UserRepository userRepository;

    private RandomStringGenerator randomGenerator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();

    /**
     * Provide a single dummy user entity without saving to database.
     *
     * @return Dummy user entity saved to database
     */
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
     * Provide as many dummy user entities as given without saving to database.
     *
     * @return List of dummy user entities saved to the database
     */
    public List<User> createMany(Integer howMany) {
        List<User> generatedUsers = new ArrayList<>();
        IntStream.range(0, howMany).forEach(i -> generatedUsers.add(createSingle()));

        return generatedUsers;
    }

    /**
     * Provide a single dummy user entity, that was saved to the database.
     *
     * @return Dummy user entity saved to database
     */
    public User createAndSaveSingle() {
        return userRepository.saveAndFlush(createSingle());
    }

    /**
     * Provide as many dummy user entities as given, that were saved to database.
     *
     * @return List of dummy user entities saved to the database
     */
    public List<User> createAndSaveMany(Integer howMany) {
        List<User> generatedUsers = new ArrayList<>();
        IntStream.range(0, howMany).forEach(i -> generatedUsers.add(createAndSaveSingle()));

        return generatedUsers;
    }
}
