package pl.lodz.p.aurora.users.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;
import pl.lodz.p.aurora.helper.UserDataFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplUnitTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private UserDataFactory dataFactory = new UserDataFactory();
    private String fakeUsername = "fakeUsername";

    @Test
    public void emptyListReturnedIfNoUsersFound() {
        // Given
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<User> returnedUsersList = userService.findAll();

        // Then
        assertThat(returnedUsersList).isNotNull().isEmpty();
    }

    @Test
    public void oneUserInListReturnedIfOneUserFound() {
        // Given
        Integer howManyUsers = 1;
        when(userRepository.findAll()).thenReturn(dataFactory.createMany(howManyUsers));

        // When
        List<User> returnedUsersList = userService.findAll();

        // Then
        assertThat(returnedUsersList).isNotNull().hasSize(howManyUsers);
    }

    @Test
    public void twoUsersInListReturnedIfTwoUsersFound() {
        // Given
        Integer howManyUsers = 2;
        when(userRepository.findAll()).thenReturn(dataFactory.createMany(howManyUsers));

        // When
        List<User> returnedUsersList = userService.findAll();

        // Then
        assertThat(returnedUsersList).isNotNull().hasSize(howManyUsers);
    }

    @Test
    public void noUserReturnedWithGivenUsername() {
        // Given
        when(userRepository.findDistinctByUsername(anyString())).thenReturn(null);

        // When
        User returnedUser = userService.findByUsername(fakeUsername);

        // Then
        assertThat(returnedUser).isNull();
    }

    @Test
    public void userReturnedWithGivenUsername() {
        // Given
        User dummyUser = dataFactory.createSingle();
        when(userRepository.findDistinctByUsername(anyString())).thenReturn(dummyUser);

        // When
        User returnedUser = userService.findByUsername(fakeUsername);

        // Then
        assertThat(returnedUser).isNotNull().isEqualTo(dummyUser);
    }
}
