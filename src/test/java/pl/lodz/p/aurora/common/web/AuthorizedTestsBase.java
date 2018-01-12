package pl.lodz.p.aurora.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.aurora.users.web.dto.UserAccountCredentialsDto;

import javax.annotation.PostConstruct;

/**
 * Created by Marek on 8/26/2017.
 */
public abstract class AuthorizedTestsBase {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("${aurora.test.credential.admin.username}")
    protected String adminUsername;
    @Value("${aurora.test.credential.admin.password}")
    private String adminPassword;
    @Value("${aurora.test.credential.unitleader.username}")
    protected String unitLeaderUsername;
    @Value("${aurora.test.credential.unitleader.password}")
    private String unitLeaderPassword;
    @Value("${aurora.test.credential.employee.username}")
    protected String employeeUsername;
    @Value("${aurora.test.credential.employee.password}")
    private String employeePassword;
    @Value("${aurora.security.jwt.token.header}")
    private String jwtTokenHeader;

    private String jwtTokenAdmin;
    private String jwtTokenUnitLeader;
    private String jwtTokenEmployee;

    @PostConstruct
    public void initAuthorizedTemplates() {
        jwtTokenAdmin = getAuthorizationToken(adminUsername, adminPassword);
        jwtTokenUnitLeader = getAuthorizationToken(unitLeaderUsername, unitLeaderPassword);
        jwtTokenEmployee = getAuthorizationToken(employeeUsername, employeePassword);
    }

    private String getAuthorizationToken(String username, String password) {
        UserAccountCredentialsDto credentials = new UserAccountCredentialsDto(username, password);
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/login", credentials, Void.class);

        return response.getHeaders().get(jwtTokenHeader).get(0);
    }

    protected <T> ResponseEntity<T> getAsAdmin(String url, Class<T> responseType) {
        return testRestTemplate.exchange(url, HttpMethod.GET, prepareHttpEntity(jwtTokenAdmin), responseType);
    }

    private HttpEntity prepareHttpEntity(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtTokenHeader, token);

        return new HttpEntity(httpHeaders);
    }

    protected <T> ResponseEntity<T> getAsUnitLeader(String url, Class<T> responseType) {
        return testRestTemplate.exchange(url, HttpMethod.GET, prepareHttpEntity(jwtTokenUnitLeader), responseType);
    }

    protected <T> ResponseEntity<T> getAsEmployee(String url, Class<T> responseType) {
        return testRestTemplate.exchange(url, HttpMethod.GET, prepareHttpEntity(jwtTokenEmployee), responseType);
    }

    protected <T> ResponseEntity<T> postAsAdmin(String url, Object request, Class<T> responseType) {
        return testRestTemplate
                .exchange(url, HttpMethod.POST, prepareHttpEntityWithPayload(jwtTokenAdmin, request), responseType);
    }

    private HttpEntity prepareHttpEntityWithPayload(String token, Object payload) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtTokenHeader, token);

        return new HttpEntity<>(payload, httpHeaders);
    }

    protected <T> ResponseEntity<T> postAsUnitLeader(String url, Object request, Class<T> responseType) {
        return testRestTemplate
                .exchange(url, HttpMethod.POST, prepareHttpEntityWithPayload(jwtTokenUnitLeader, request), responseType);
    }

    protected <T> ResponseEntity<T> postAsEmployee(String url, Object request, Class<T> responseType) {
        return testRestTemplate
                .exchange(url, HttpMethod.POST, prepareHttpEntityWithPayload(jwtTokenEmployee, request), responseType);
    }

    protected <T> ResponseEntity<T> putAsAdmin(String url, String eTag, Object request, Class<T> responseType) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                prepareHttpEntityWithPayloadAndETag(jwtTokenAdmin, eTag, request),
                responseType);
    }

    private HttpEntity prepareHttpEntityWithPayloadAndETag(String token, String eTag, Object payload) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtTokenHeader, token);

        if (eTag != null && !eTag.isEmpty()) {
            httpHeaders.add("ETag", eTag);
        }

        return new HttpEntity<>(payload, httpHeaders);
    }

    protected <T> ResponseEntity<T> putAsUnitLeader(String url, String eTag, Object request, Class<T> responseType) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                prepareHttpEntityWithPayloadAndETag(jwtTokenUnitLeader, eTag, request),
                responseType);
    }

    protected <T> ResponseEntity<T> putAsEmployee(String url, String eTag, Object request, Class<T> responseType) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                prepareHttpEntityWithPayloadAndETag(jwtTokenEmployee, eTag, request),
                responseType);
    }
}
