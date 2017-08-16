package pl.lodz.p.aurora.web;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.domain.dto.UserDto;
import pl.lodz.p.aurora.helper.UserDtoDataFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTests {

    @Autowired
    private UserDtoDataFactory dataFactory;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private String featureUrl = "/api/users/";

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
        UserDto savedUser = dataFactory.createAndSaveSingle();

        // When
        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).contains(savedUser);
    }

    @Test
    public void twoUsersInListReturnedWhenDatabaseContainsTwoUsers() throws Exception {
        // Given
        List<UserDto> savedUsersList = dataFactory.createAndSaveMany(2);

        // When
        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity(featureUrl, UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2).contains(savedUsersList.get(0));
    }

    @Test
    public void noContentResponseIfNoUserFoundWithGivenUsername() {
        // Given
        String findByUsernameUrl = "/api/users/some+fake+username";

        // When
        ResponseEntity<UserDto> response = testRestTemplate.getForEntity(findByUsernameUrl, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void responseWithUserIfUserFoundWithGivenUsername() {
        // Given
        UserDto savedUser = dataFactory.createAndSaveSingle();
        String findByUsernameUrl = "/api/users/" + savedUser.getName();

        // When
        ResponseEntity<UserDto> response = testRestTemplate.getForEntity(findByUsernameUrl, UserDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(savedUser);
    }
}
