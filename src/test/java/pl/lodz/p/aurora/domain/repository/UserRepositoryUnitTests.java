package pl.lodz.p.aurora.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.domain.entity.User;
import pl.lodz.p.aurora.helper.UserDataFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryUnitTests {

    @Autowired private UserRepository userRepository;
    @Autowired private UserDataFactory dataFactory;

    @Test
    public void allUsersWereReturnedFromDatabase() {
        // Given
        Integer howManyDummyUsers = 5;
        List<User> testData = dataFactory.createMany(howManyDummyUsers);

        // When
        List<User> dataReturnedByRepository = userRepository.findAll();

        // Then
        assertThat(dataReturnedByRepository).isNotNull().isNotEmpty().isEqualTo(testData);
    }

    @Test
    public void noUsersWereReturnedFromDatabase() {
        // Then
        assertThat(userRepository.findAll()).isNotNull().isEmpty();
    }

    @Test
    public void foundUserWithGivenUsernameInDatabase() {
        // Given
        User dummyUser = dataFactory.createSingle();

        // When
        User userReturnedByRepository = userRepository.findByUsername(dummyUser.getUsername());

        // Then
        assertThat(userReturnedByRepository).isNotNull().isEqualTo(dummyUser);
    }

    @Test
    public void notFoundUserWithGivenUsernameInDatabase() {
        // Given
        String nonExistingUsername = "this name is strange";

        // When
        User userReturnedByRepository = userRepository.findByUsername(nonExistingUsername);

        // Then
        assertThat(userReturnedByRepository).isNull();
    }
}
