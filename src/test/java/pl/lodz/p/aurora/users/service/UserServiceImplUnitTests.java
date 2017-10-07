package pl.lodz.p.aurora.users.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import pl.lodz.p.aurora.common.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.helper.RoleDataFactory;
import pl.lodz.p.aurora.helper.UserDataFactory;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

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
    private String fakeETag = "fakeETag";

//    @Test
//    public void emptyListReturnedIfNoUsersFound() {
//        // Given
//        when(userRepository.findAllByOrderBySurnameAscNameAsc()).thenReturn(new ArrayList<>());
//
//        // When
//        List<User> returnedUsersList = userService.findAllByOrderBySurnameAscNameAsc();
//
//        // Then
//        assertThat(returnedUsersList).isNotNull().isEmpty();
//    }
//
//    @Test
//    public void oneUserInListReturnedIfOneUserFound() {
//        // Given
//        Integer howManyUsers = 1;
//        when(userRepository.findAllByOrderBySurnameAscNameAsc()).thenReturn(userDataFactory.createMany(howManyUsers));
//
//        // When
//        List<User> returnedUsersList = userService.findAllByOrderBySurnameAscNameAsc();
//
//        // Then
//        assertThat(returnedUsersList).isNotNull().hasSize(howManyUsers);
//    }
//
//    @Test
//    public void twoUsersInListReturnedIfTwoUsersFound() {
//        // Given
//        Integer howManyUsers = 2;
//        when(userRepository.findAllByOrderBySurnameAscNameAsc()).thenReturn(userDataFactory.createMany(howManyUsers));
//
//        // When
//        List<User> returnedUsersList = userService.findAllByOrderBySurnameAscNameAsc();
//
//        // Then
//        assertThat(returnedUsersList).isNotNull().hasSize(howManyUsers);
//    }

    @Test(expected = InvalidResourceRequestedException.class)
    public void noUserReturnedWithGivenUsername() {
        // Given
        when(userRepository.findDistinctByUsername(anyString())).thenReturn(null);

        // When
        User returnedUser = userService.findByUsername(fakeUsername);
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
        when(userRepository.saveAndFlush(dummyUser)).thenThrow(javax.validation.ConstraintViolationException.class);

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
    public void failIfCreatedUserIsAnInvalidEntityAsAdmin() {
        // Given
        User dummyUser = userDataFactory.createSingle();
        org.hibernate.exception.ConstraintViolationException exceptionCause =
                new org.hibernate.exception.ConstraintViolationException(null, null, null);
        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("constraint [null]", exceptionCause));

        // Expect
        Throwable thrown = catchThrowable(() -> userService.createAsAdmin(dummyUser));

        // Then
        assertThat(thrown).isInstanceOf(InvalidEntityStateException.class);
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

//    @Test
//    public void returnProperlyUpdatedUser() {
//        // Given
//        String newName = "Some New Name";
//        User storedUser = userDataFactory.createSingle();
//        User userPassedToMethod = storedUser.clone();
//        userPassedToMethod.setName(newName);
//        when(userRepository.findDistinctByUsername(storedUser.getUsername())).thenReturn(storedUser);
//        when(versionTransformer.hash(anyLong())).thenReturn(fakeETag);
//        when(userRepository.saveAndFlush(any(User.class))).thenReturn(storedUser);
//
//        // When
//        User updatedUser = userService.updateOtherAccount(fakeETag, userPassedToMethod);
//
//        // Then
//        assertThat(updatedUser).isNotNull();
//        assertThat(updatedUser.getName()).isEqualTo(newName);
//    }
//
//    @Test(expected = OutdatedEntityModificationException.class)
//    public void failOnValidationETagHeaderWhileUpdatingUser() {
//        // Given
//        String invalidETag = "invalidETag";
//        User storedUser = userDataFactory.createSingle();
//        User userPassedToMethod = storedUser.clone();
//        when(userRepository.findDistinctByUsername(storedUser.getUsername())).thenReturn(storedUser);
//        when(versionTransformer.hash(anyLong())).thenReturn(fakeETag);
//
//        // When-then
//        userService.updateOtherAccount(invalidETag, userPassedToMethod);
//    }

    @Test(expected = InvalidResourceRequestedException.class)
    public void failOnTryingToUpdateNonExistingUser() {
        // Given
        String invalidETag = "etag";
        User dummyUser = userDataFactory.createSingle();
        when(userRepository.findDistinctByUsername(dummyUser.getUsername())).thenReturn(null);

        // When-then
        userService.updateOtherAccount(invalidETag, dummyUser);
    }

//    @Test
//    public void returnUpdatedUserWithEnabledState() {
//        // Given
//        User dummyUser = prepareForUserWithStateTest(false);
//
//        // When
//        User updatedUsed = userService.enable(dummyUser.getId(), fakeETag);
//
//        // Then
//        assertThat(updatedUsed.isEnabled()).isTrue();
//    }

//    private User prepareForUserWithStateTest(boolean state) {
//        User dummyUser = userDataFactory.createSingle();
//        dummyUser.setEnabled(state);
//        when(userRepository.findOne(dummyUser.getId())).thenReturn(dummyUser);
//        when(userRepository.saveAndFlush(dummyUser)).thenReturn(dummyUser);
//        when(versionTransformer.hash(anyLong())).thenReturn(fakeETag);
//
//        return dummyUser;
//    }


    @Test(expected = InvalidResourceRequestedException.class)
    public void failOnTryingToEnableNonExistingUser() {
        // Given
        User dummyUser = userDataFactory.createSingle();
        when(userRepository.findOne(dummyUser.getId())).thenReturn(null);

        // When-then
        userService.enable(dummyUser.getId(), fakeETag);
    }

//    @Test(expected = OutdatedEntityModificationException.class)
//    public void failOnValidationETagHeaderWhileEnablingUser() {
//        // Given
//        String invalidETag = "invalidETag";
//        User dummyUser = userDataFactory.createSingle();
//        when(userRepository.findOne(dummyUser.getId())).thenReturn(dummyUser);
//        when(versionTransformer.hash(anyLong())).thenReturn(fakeETag);
//
//        // When-then
//        userService.enable(dummyUser.getId(), invalidETag);
//    }
//
//    @Test
//    public void returnUpdatedUserWithDisabledState() {
//        // Given
//        User dummyUser = prepareForUserWithStateTest(true);
//
//        // When
//        User updatedUsed = userService.disable(dummyUser.getId(), fakeETag);
//
//        // Then
//        assertThat(updatedUsed.isEnabled()).isFalse();
//    }
//
//    @Test
//    public void returnUserWithProperlyAssignedRole() {
//        // Given
//        Role dummyRole = roleDataFactory.createSingle();
//        User dummyUser = userDataFactory.createSingle();
//        dummyUser.setId(1L);
//        when(roleService.findByName(dummyRole.getName())).thenReturn(dummyRole);
//        when(userRepository.findOne(dummyUser.getId())).thenReturn(dummyUser);
//        when(versionTransformer.hash(anyLong())).thenReturn(fakeETag);
//        when(userRepository.saveAndFlush(dummyUser)).thenReturn(dummyUser);
//
//        // When
//        User userWithAssignedRole = userService.assignRole(dummyUser.getId(), dummyRole.getName(), fakeETag);
//
//        // Then
//        assertThat(userWithAssignedRole.getRoles()).isNotEmpty().contains(dummyRole);
//    }
//
//    @Test(expected = InvalidResourceRequestedException.class)
//    public void failWhileTryingToAssignNonExistingRole() {
//        // Given
//        Role dummyRole = roleDataFactory.createSingle();
//        User dummyUser = userDataFactory.createSingle();
//        dummyUser.setId(1L);
//        when(roleService.findByName(dummyRole.getName())).thenThrow(InvalidResourceRequestedException.class);
//
//        // When-then
//        userService.assignRole(dummyUser.getId(), dummyRole.getName(), fakeETag);
//    }
}
