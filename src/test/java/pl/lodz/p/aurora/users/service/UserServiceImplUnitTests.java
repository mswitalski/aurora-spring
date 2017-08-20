package pl.lodz.p.aurora.users.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import pl.lodz.p.aurora.common.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.helper.RoleDataFactory;
import pl.lodz.p.aurora.helper.UserDataFactory;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplUnitTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private UserServiceImpl userService;

    private UserDataFactory userDataFactory = new UserDataFactory();
    private RoleDataFactory roleDataFactory = new RoleDataFactory();
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
        when(userRepository.findAll()).thenReturn(userDataFactory.createMany(howManyUsers));

        // When
        List<User> returnedUsersList = userService.findAll();

        // Then
        assertThat(returnedUsersList).isNotNull().hasSize(howManyUsers);
    }

    @Test
    public void twoUsersInListReturnedIfTwoUsersFound() {
        // Given
        Integer howManyUsers = 2;
        when(userRepository.findAll()).thenReturn(userDataFactory.createMany(howManyUsers));

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
        User dummyUser = userDataFactory.createSingle();
        when(userRepository.findDistinctByUsername(anyString())).thenReturn(dummyUser);

        // When
        User returnedUser = userService.findByUsername(fakeUsername);

        // Then
        assertThat(returnedUser).isNotNull().isEqualTo(dummyUser);
    }

    @Test
    public void returnProperlyCreatedUserAsAdmin() {
        // Given
        User dummyUser = userDataFactory.createSingle();
        when(userRepository.saveAndFlush(dummyUser)).thenReturn(dummyUser);

        // When
        User savedUserEntity = userService.createAsAdmin(dummyUser);

        // Then
        assertThat(savedUserEntity).isNotNull().isEqualTo(dummyUser);
    }

    @Test
    public void returnProperlyCreatedUserAsUnitLeader() {
        // Given
        User dummyUser = userDataFactory.createSingle();
        Role dummyRole = roleDataFactory.createSingle();
        when(userRepository.saveAndFlush(dummyUser)).thenReturn(dummyUser);
        when(roleService.findByName(any())).thenReturn(dummyRole);

        // When
        User savedUserEntity = userService.createAsUnitLeader(dummyUser);

        // Then
        assertThat(savedUserEntity).isNotNull().isEqualTo(dummyUser);
        assertThat(savedUserEntity.getRoles()).isNotNull().contains(dummyRole);
    }

    @Test(expected = InvalidEntityStateException.class)
    public void failToCreateUserWithNullFieldsAsAdmin() {
        // Given
        User dummyUser = new User();
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(ConstraintViolationException.class);

        // Then
        userService.createAsAdmin(dummyUser);
    }

    @Test
    public void failIfCreatedUserHasNotUniqueUsernameAsAdmin() {
        // Given
        User dummyUser = userDataFactory.createSingle();
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        when(userRepository.findDistinctByUsername(anyString())).thenReturn(new User());
        when(userRepository.findDistinctByEmail(anyString())).thenReturn(null);

        // Expect
        Throwable thrown = catchThrowable(() -> userService.createAsAdmin(dummyUser));

        // Then
        assertThat(thrown).isInstanceOf(UniqueConstraintViolationException.class);
        assertThat(((UniqueConstraintViolationException) thrown).getEntityName())
                .isEqualTo(UserDto.class.getSimpleName());
        assertThat(((UniqueConstraintViolationException) thrown).getFieldsNames())
                .isNotEmpty().contains("username");
    }

    @Test
    public void failIfCreatedUserHasNotUniqueEmailAddressAsAdmin() {
        // Given
        User dummyUser = userDataFactory.createSingle();
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        when(userRepository.findDistinctByUsername(anyString())).thenReturn(null);
        when(userRepository.findDistinctByEmail(anyString())).thenReturn(new User());

        // Expect
        Throwable thrown = catchThrowable(() -> userService.createAsAdmin(dummyUser));

        // Then
        assertThat(thrown).isInstanceOf(UniqueConstraintViolationException.class);
        assertThat(((UniqueConstraintViolationException) thrown).getEntityName())
                .isEqualTo(UserDto.class.getSimpleName());
        assertThat(((UniqueConstraintViolationException) thrown).getFieldsNames())
                .isNotEmpty().contains("email");
    }
}
