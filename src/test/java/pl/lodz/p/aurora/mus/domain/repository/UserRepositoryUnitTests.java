package pl.lodz.p.aurora.mus.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.helper.UserDataFactory;
import pl.lodz.p.aurora.mus.domain.entity.User;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryUnitTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDataFactory dataFactory;

//    @Test
//    public void allBasicUsersWereReturnedFromDatabase() {
//        // Given
//        Integer howManyBasicUsers = 3;
//
//        // When
//        List<User> dataReturnedByRepository = userRepository.findAllByOrderBySurnameAscNameAsc();
//
//        // Then
//        assertThat(dataReturnedByRepository).isNotNull().isNotEmpty().hasSize(howManyBasicUsers);
//    }

    @Test
    public void foundUserWithGivenUsernameInDatabase() {
        // Given
        User dummyUser = dataFactory.createAndSaveSingle();

        // When
        User userReturnedByRepository = userRepository.findDistinctByUsername(dummyUser.getUsername());

        // Then
        assertThat(userReturnedByRepository).isNotNull().isEqualTo(dummyUser);
    }

    @Test
    public void notFoundUserWithGivenUsernameInDatabase() {
        // Given
        String nonExistingUsername = "this name is strange";

        // When
        User userReturnedByRepository = userRepository.findDistinctByUsername(nonExistingUsername);

        // Then
        assertThat(userReturnedByRepository).isNull();
    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyUserIsNotSavedToDatabase() {
        // Given
        User dummyUser = new User();

        // When
        userRepository.saveAndFlush(dummyUser);
    }
}
