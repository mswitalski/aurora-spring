package pl.lodz.p.aurora.mus.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.common.web.dto.ValidationMessageDto;
import pl.lodz.p.aurora.common.web.AuthorizedTestsBase;
import pl.lodz.p.aurora.helper.*;
import pl.lodz.p.aurora.mus.web.dto.UserDto;
import pl.lodz.p.aurora.mus.domain.entity.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTests extends AuthorizedTestsBase {

    @Autowired
    private UserDtoDataFactory userDataFactory;
    @Autowired
    private RoleDataFactory roleDataFactory;
    @Value("${aurora.default.role.name}")
    private String defaultEmployeeRoleName;
    private final String featureUrl = "/api/users/";
    private final String featureAdminUrl = "/api/admin/users/";
    private final String featureUnitLeaderUrl = "/api/unitleader/users/";

    @Test
    public void basicUsersInListReturnedWhenDatabaseContainsOnlyBasicUsers() throws Exception {
        // When
        ResponseEntity<UserDto[]> response = getAsAdmin(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(3);
    }

    @Test
    public void userInReturnedListWhenDatabaseContainsOneUser() throws Exception {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();

        // When
        ResponseEntity<UserDto[]> response = getAsAdmin(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(savedUser);
    }

    @Test
    public void twoAdditionalUsersInListReturnedWhenDatabaseContainsTwoAdditionalUsers() throws Exception {
        // Given
        List<UserDto> savedUsersList = userDataFactory.createAndSaveMany(2);

        // When
        ResponseEntity<UserDto[]> response = getAsAdmin(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5).contains(savedUsersList.get(0));
    }

    @Test
    public void noContentResponseIfNoUserFoundWithGivenUsername() {
        // Given
        String fakeUsername = "some+fake+username";
        String findByUsernameUrl = featureUrl + fakeUsername;

        // When
        ResponseEntity<UserDto> response = getAsAdmin(findByUsernameUrl, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void responseWithUserIfUserFoundWithGivenUsername() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        String findByUsernameUrl = featureUrl + savedUser.getName();

        // When
        ResponseEntity<UserDto> response = getAsAdmin(findByUsernameUrl, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(savedUser);
    }

    @Test
    public void returnProperlyCreatedUserAsAdmin() {
        // Given
        UserDto dummyUser = userDataFactory.createSingleWithRandomRole();

        // When
        ResponseEntity<UserDto> response = postAsAdmin(featureAdminUrl, dummyUser, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(dummyUser.getUsername());
        assertThat(response.getBody().getRoles()).contains(dummyUser.getRoles().iterator().next());
    }

    @Test
    public void returnProperlyCreatedUserAsUnitLeader() {
        // Given
        UserDto dummyUser = userDataFactory.createSingle();

        // When
        ResponseEntity<UserDto> response = postAsUnitLeader(featureUnitLeaderUrl, dummyUser, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(dummyUser.getUsername());
        assertThat(response.getBody().getRoles().size()).isEqualTo(1);
        assertThat(response.getBody().getRoles().stream().findFirst().get().getName()).isEqualTo(defaultEmployeeRoleName);
    }

    @Test
    public void failToCreateUserWithNullFieldsAsAdmin() {
        // Given
        UserDto dummyUser = new UserDto();

        // When
        ResponseEntity<ValidationMessageDto[]> response =
                postAsAdmin(featureAdminUrl, dummyUser, ValidationMessageDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    public void failIfCreatedUserHasNotUniqueUsernameAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        UserDto newUserWithSameUsername = userDataFactory.createSingle();
        newUserWithSameUsername.setUsername(savedUser.getUsername());

        // When
        ResponseEntity<ValidationMessageDto[]> response =
                postAsAdmin(featureAdminUrl, newUserWithSameUsername, ValidationMessageDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getFieldName()).isEqualTo("username");
    }

    @Test
    public void failIfCreatedUserHasNotUniqueEmailAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        UserDto newUserWithSameEmail = userDataFactory.createSingle();
        newUserWithSameEmail.setEmail(savedUser.getEmail());

        // When
        ResponseEntity<ValidationMessageDto[]> response =
                postAsAdmin(featureAdminUrl, newUserWithSameEmail, ValidationMessageDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getFieldName()).isEqualTo("email");
    }

    @Test
    public void failIfCreatedUserHasNotUniqueEmailAndUsernameAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        UserDto newUserWithSameUsernameAndEmail = userDataFactory.createSingle();
        newUserWithSameUsernameAndEmail.setUsername(savedUser.getUsername());
        newUserWithSameUsernameAndEmail.setEmail(savedUser.getEmail());

        // When
        ResponseEntity<ValidationMessageDto[]> response =
                postAsAdmin(featureAdminUrl, newUserWithSameUsernameAndEmail, ValidationMessageDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().length).isEqualTo(2);
        assertThat(response.getBody()[1].getFieldName()).isEqualTo("username");
        assertThat(response.getBody()[0].getFieldName()).isEqualTo("email");
    }

    @Test
    public void returnProperlyUpdatedUserAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = putAsAdmin(
                featureAdminUrl,
                responseOnGetUserDto.getHeaders().getETag(),
                fetchedUserBeforeUpdate,
                UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnUpdateUserDto.getBody().getName()).isEqualTo(newName);
    }

    @Test
    public void returnProperlyUpdatedUserAsUnitLeader() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = putAsUnitLeader(
                featureUnitLeaderUrl,
                responseOnGetUserDto.getHeaders().getETag(),
                fetchedUserBeforeUpdate,
                UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnUpdateUserDto.getBody().getName()).isEqualTo(newName);
    }

    @Test
    public void failToUpdateAnotherUserAsEmployee() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = putAsEmployee(
                featureUrl,
                responseOnGetUserDto.getHeaders().getETag(),
                fetchedUserBeforeUpdate,
                UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void returnProperlyUpdatedOwnAccountAsEmployee() {
        // Given
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + employeeUsername, UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = putAsEmployee(
                featureUrl,
                responseOnGetUserDto.getHeaders().getETag(),
                fetchedUserBeforeUpdate,
                UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnUpdateUserDto.getBody().getName()).isEqualTo(newName);
    }

    @Test
    public void failOnUpdatingUserWithoutRequiredETagHeaderAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = putAsAdmin(
                featureAdminUrl,
                "",
                fetchedUserBeforeUpdate,
                UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void failOnSimultaneousUserUpdatesAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdateFirstUpdate = responseOnGetUserDto.getBody();
        UserDto fetchedUserBeforeUpdateOtherUpdate = fetchedUserBeforeUpdateFirstUpdate.clone();
        String newName = "some new name";
        String newNameOnSecondUpdate = "on second update new name";
        fetchedUserBeforeUpdateFirstUpdate.setName(newName);
        fetchedUserBeforeUpdateOtherUpdate.setName(newNameOnSecondUpdate);

        // When
        ResponseEntity<UserDto> responseOnFirstUpdateUserDto = putAsAdmin(
                featureAdminUrl,
                responseOnGetUserDto.getHeaders().getETag(),
                fetchedUserBeforeUpdateFirstUpdate,
                UserDto.class);
        ResponseEntity<UserDto> responseOnOtherUpdateUserDto = putAsAdmin(
                featureAdminUrl,
                responseOnGetUserDto.getHeaders().getETag(),
                fetchedUserBeforeUpdateOtherUpdate,
                UserDto.class);

        // Then
        assertThat(responseOnFirstUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnFirstUpdateUserDto.getBody().getName()).isEqualTo(newName);
        assertThat(responseOnOtherUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void failOnTryingToUpdateNonExistingUserAsAdmin() {
        // Given
        String fakeETag = "fakeETag";
        UserDto savedUser = userDataFactory.createSingle();

        // When
        ResponseEntity<UserDto> responseOnFirstUpdateUserDto = putAsAdmin(featureAdminUrl, fakeETag, savedUser, UserDto.class);

        // Then
        assertThat(responseOnFirstUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void returnEnabledUser() {
        // Given
        UserDto dummyUser = userDataFactory.createSingle();
        dummyUser.setEnabled(false);
        ResponseEntity<UserDto> responseOnCreated = postAsAdmin(featureAdminUrl, dummyUser, UserDto.class);
        dummyUser = responseOnCreated.getBody();
        String url = featureAdminUrl + dummyUser.getId() + "/activation";

        // When
        ResponseEntity<UserDto> responseOnPatched = putAsAdmin(
                url,
                responseOnCreated.getHeaders().getETag(),
                null,
                UserDto.class);

        // Then
        assertThat(responseOnPatched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnPatched.getBody().isEnabled()).isTrue();
    }

    @Test
    public void returnDisabledUser() {
        // Given
        UserDto dummyUser = userDataFactory.createSingle();
        dummyUser.setEnabled(true);
        ResponseEntity<UserDto> responseOnCreated = postAsAdmin(featureAdminUrl, dummyUser, UserDto.class);
        dummyUser = responseOnCreated.getBody();
        String url = featureAdminUrl + dummyUser.getId() + "/deactivation";

        // When
        ResponseEntity<UserDto> responseOnPatched = putAsAdmin(
                url,
                responseOnCreated.getHeaders().getETag(),
                null,
                UserDto.class);

        // Then
        assertThat(responseOnPatched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnPatched.getBody().isEnabled()).isFalse();
    }

    @Test
    public void returnUserWithProperlyAssignedRole() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        Role savedRole = roleDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = getAsAdmin(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String url = featureAdminUrl + fetchedUserBeforeUpdate.getId() + "/role/" + savedRole.getName();

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = putAsAdmin(
                url,
                responseOnGetUserDto.getHeaders().getETag(),
                null,
                UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnUpdateUserDto.getBody().getRoles()).hasSize(1).contains(savedRole);
    }
}
