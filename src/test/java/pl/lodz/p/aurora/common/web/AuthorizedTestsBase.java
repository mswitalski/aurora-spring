package pl.lodz.p.aurora.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by Marek on 8/26/2017.
 */
public abstract class AuthorizedTestsBase {

    @Autowired
    protected TestRestTemplate testRestTemplate;
    protected TestRestTemplate testRestTemplateAsAdmin;
    protected TestRestTemplate testRestTemplateAsUnitLeader;
    protected TestRestTemplate testRestTemplateAsEmployee;
    @Value("${aurora.test.credential.admin.username}")
    protected String adminUsername;
    @Value("${aurora.test.credential.admin.password}")
    protected String adminPassword;
    @Value("${aurora.test.credential.unitleader.username}")
    protected String unitLeaderUsername;
    @Value("${aurora.test.credential.unitleader.password}")
    protected String unitLeaderPassword;
    @Value("${aurora.test.credential.employee.username}")
    protected String employeeUsername;
    @Value("${aurora.test.credential.employee.password}")
    protected String employeePassword;

    @PostConstruct
    public void initAuthorizedTemplates() {
        testRestTemplateAsAdmin = testRestTemplate.withBasicAuth(adminUsername, adminPassword);
        testRestTemplateAsUnitLeader = testRestTemplate.withBasicAuth(unitLeaderUsername, unitLeaderPassword);
        testRestTemplateAsEmployee = testRestTemplate.withBasicAuth(employeeUsername, employeePassword);
    }
}
