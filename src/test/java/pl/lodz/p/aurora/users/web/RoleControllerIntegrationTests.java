package pl.lodz.p.aurora.users.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.users.domain.entity.Role;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleControllerIntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private final String featureUrl = "/api/roles/";

    @Test
    public void threeRolesReturnedFromDatabase() {
        // Given
        Integer expectedNumberOfRoles = 3;

        // When
        ResponseEntity<Role[]> response = testRestTemplate.getForEntity(featureUrl, Role[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isEqualTo(expectedNumberOfRoles);
    }
}