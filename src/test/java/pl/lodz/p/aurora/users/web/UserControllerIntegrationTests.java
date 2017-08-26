package pl.lodz.p.aurora.users.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.common.domain.dto.ValidationMessageDto;
import pl.lodz.p.aurora.helper.RoleDataFactory;
import pl.lodz.p.aurora.helper.UserDtoDataFactory;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTests {

    @Autowired
    private UserDtoDataFactory userDataFactory;
    @Autowired
    private RoleDataFactory roleDataFactory;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("${aurora.default.role.name}")
    private String defaultEmployeeRoleName;
    private final String featureUrl = "/api/users/";
    private final String featureAdminUrl = "/api/admin/users/";
    private final String featureUnitLeaderUrl = "/api/unitleader/users/";

    @Test
    public void emptyUsersListReturnedWhenDatabaseIsEmpty() throws Exception {
        // When
        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void oneUserInListReturnedWhenDatabaseContainsOneUser() throws Exception {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();

        // When
        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).contains(savedUser);
    }

    @Test
    public void twoUsersInListReturnedWhenDatabaseContainsTwoUsers() throws Exception {
        // Given
        List<UserDto> savedUsersList = userDataFactory.createAndSaveMany(2);

        // When
        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2).contains(savedUsersList.get(0));
    }

    @Test
    public void noContentResponseIfNoUserFoundWithGivenUsername() {
        // Given
        String fakeUsername = "some+fake+username";
        String findByUsernameUrl = featureUrl + fakeUsername;

        // When
        ResponseEntity<UserDto> response = testRestTemplate.getForEntity(findByUsernameUrl, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void responseWithUserIfUserFoundWithGivenUsername() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        String findByUsernameUrl = featureUrl + savedUser.getName();

        // When
        ResponseEntity<UserDto> response = testRestTemplate.getForEntity(findByUsernameUrl, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(savedUser);
    }

    @Test
    public void returnProperlyCreatedUserAsAdmin() {
        // Given
        UserDto dummyUser = userDataFactory.createSingleWithRandomRole();

        // When
        ResponseEntity<UserDto> response = testRestTemplate.postForEntity(featureAdminUrl, dummyUser, UserDto.class);

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
        ResponseEntity<UserDto> response = testRestTemplate.postForEntity(featureUnitLeaderUrl, dummyUser, UserDto.class);

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
                testRestTemplate.postForEntity(featureAdminUrl, dummyUser, ValidationMessageDto[].class);

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
                testRestTemplate.postForEntity(featureAdminUrl, newUserWithSameUsername, ValidationMessageDto[].class);

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
                testRestTemplate.postForEntity(featureAdminUrl, newUserWithSameEmail, ValidationMessageDto[].class);

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
                testRestTemplate.postForEntity(featureAdminUrl, newUserWithSameUsernameAndEmail, ValidationMessageDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().length).isEqualTo(2);
        assertThat(response.getBody()[1].getFieldName()).isEqualTo("username");
        assertThat(response.getBody()[0].getFieldName()).isEqualTo("email");
    }

    @Test
    public void returnProperlyUpdatedUserAsAdmin() {
        updateTest(featureAdminUrl);
    }

    private void updateTest(String url) {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = testRestTemplate
                .getForEntity(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", responseOnGetUserDto.getHeaders().getETag());
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(fetchedUserBeforeUpdate, httpHeaders);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = testRestTemplate
                .exchange(url, HttpMethod.PUT, httpEntity, UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnUpdateUserDto.getBody().getName()).isEqualTo(newName);
    }

    @Test
    public void returnProperlyUpdatedUserAsUnitLeader() {
        updateTest(featureUnitLeaderUrl);
    }

    @Test
    public void returnProperlyUpdatedUserAsUser() {
        updateTest(featureUrl);
    }

    @Test
    public void failOnUpdatingUserWithoutRequiredETagHeaderAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = testRestTemplate
                .getForEntity(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        String newName = "some new name";
        fetchedUserBeforeUpdate.setName(newName);
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(fetchedUserBeforeUpdate);

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = testRestTemplate
                .exchange(featureAdminUrl, HttpMethod.PUT, httpEntity, UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void failOnSimultaneousUserUpdatesAsAdmin() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = testRestTemplate
                .getForEntity(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdateFirstUpdate = responseOnGetUserDto.getBody();
        UserDto fetchedUserBeforeUpdateOtherUpdate = fetchedUserBeforeUpdateFirstUpdate.clone();
        String newName = "some new name";
        String newNameOnSecondUpdate = "on second update new name";
        fetchedUserBeforeUpdateFirstUpdate.setName(newName);
        fetchedUserBeforeUpdateOtherUpdate.setName(newNameOnSecondUpdate);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", responseOnGetUserDto.getHeaders().getETag());
        HttpEntity<UserDto> httpEntityFirstUpdate = new HttpEntity<>(fetchedUserBeforeUpdateFirstUpdate, httpHeaders);
        HttpEntity<UserDto> httpEntityOtherUpdate = new HttpEntity<>(fetchedUserBeforeUpdateOtherUpdate, httpHeaders);

        // When
        ResponseEntity<UserDto> responseOnFirstUpdateUserDto = testRestTemplate
                .exchange(featureAdminUrl, HttpMethod.PUT, httpEntityFirstUpdate, UserDto.class);
        ResponseEntity<UserDto> responseOnOtherUpdateUserDto = testRestTemplate
                .exchange(featureAdminUrl, HttpMethod.PUT, httpEntityOtherUpdate, UserDto.class);

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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", fakeETag);
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(savedUser, httpHeaders);

        // When
        ResponseEntity<UserDto> responseOnFirstUpdateUserDto = testRestTemplate
                .exchange(featureAdminUrl, HttpMethod.PUT, httpEntity, UserDto.class);

        // Then
        assertThat(responseOnFirstUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void returnEnabledUser() {
        // Given
        UserDto dummyUser = userDataFactory.createSingle();
        dummyUser.setEnabled(false);
        ResponseEntity<UserDto> responseOnCreated = testRestTemplate.postForEntity(featureAdminUrl, dummyUser, UserDto.class);
        dummyUser = responseOnCreated.getBody();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", responseOnCreated.getHeaders().getETag());
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(httpHeaders);
        String url = featureAdminUrl + dummyUser.getId() + "/activation";
        System.out.println(url);

        // When
        ResponseEntity<UserDto> responseOnPatched = testRestTemplate
                .exchange(url, HttpMethod.PUT, httpEntity, UserDto.class);

        // Then
        assertThat(responseOnPatched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnPatched.getBody().isEnabled()).isTrue();
    }

    @Test
    public void returnDisabledUser() {
        // Given
        UserDto dummyUser = userDataFactory.createSingle();
        dummyUser.setEnabled(true);
        ResponseEntity<UserDto> responseOnCreated = testRestTemplate.postForEntity(featureAdminUrl, dummyUser, UserDto.class);
        dummyUser = responseOnCreated.getBody();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", responseOnCreated.getHeaders().getETag());
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(httpHeaders);
        String url = featureAdminUrl + dummyUser.getId() + "/deactivation";
        System.out.println(url);

        // When
        ResponseEntity<UserDto> responseOnPatched = testRestTemplate
                .exchange(url, HttpMethod.PUT, httpEntity, UserDto.class);

        // Then
        assertThat(responseOnPatched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnPatched.getBody().isEnabled()).isFalse();
    }

    @Test
    public void returnUserWithProperlyAssignedRole() {
        // Given
        UserDto savedUser = userDataFactory.createAndSaveSingle();
        Role savedRole = roleDataFactory.createAndSaveSingle();
        ResponseEntity<UserDto> responseOnGetUserDto = testRestTemplate
                .getForEntity(featureUrl + savedUser.getUsername(), UserDto.class);
        UserDto fetchedUserBeforeUpdate = responseOnGetUserDto.getBody();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", responseOnGetUserDto.getHeaders().getETag());
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(httpHeaders);
        String url = featureAdminUrl + fetchedUserBeforeUpdate.getId() + "/role/" + savedRole.getName();
        System.out.println(url);
        System.out.println(responseOnGetUserDto.getHeaders().getETag());

        // When
        ResponseEntity<UserDto> responseOnUpdateUserDto = testRestTemplate
                .exchange(url, HttpMethod.PUT, httpEntity, UserDto.class);

        // Then
        assertThat(responseOnUpdateUserDto.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseOnUpdateUserDto.getBody().getRoles()).hasSize(1).contains(savedRole);
    }
}
