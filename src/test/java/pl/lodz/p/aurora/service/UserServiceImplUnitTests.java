package pl.lodz.p.aurora.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.aurora.domain.entity.User;
import pl.lodz.p.aurora.domain.repository.UserRepository;
import pl.lodz.p.aurora.helper.UserDataFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplUnitTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private UserDataFactory dataFactory = new UserDataFactory();

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
        when(userRepository.findAll()).thenReturn(dataFactory.createMany(1));

        // When
        List<User> returnedUsersList = userService.findAll();

        // Then
        assertThat(returnedUsersList).isNotNull().hasSize(1);
    }

    @Test
    public void twoUsersInListReturnedIfTwoUsersFound() {
        // Given
        when(userRepository.findAll()).thenReturn(dataFactory.createMany(2));

        // When
        List<User> returnedUsersList = userService.findAll();

        // Then
        assertThat(returnedUsersList).isNotNull().hasSize(2);
    }
}
