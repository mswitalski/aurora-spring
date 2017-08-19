package pl.lodz.p.aurora.users.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.helper.RoleDataFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryUnitTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleDataFactory dataFactory;

    @Test
    public void noRolesWereReturnedFromDatabase() {
        // Then
        assertThat(roleRepository.findAll()).isNotNull().isEmpty();
    }

    @Test
    public void allRolesWereReturnedFromDatabase() {
        // Given
        Integer howManyDummyRoles = 5;
        List<Role> testData = dataFactory.createAndSaveMany(howManyDummyRoles);

        // When
        List<Role> dataReturnedByRepository = roleRepository.findAll();

        // Then
        assertThat(dataReturnedByRepository).isNotNull().isNotEmpty().contains(testData.get(0));
    }

    @Test
    public void foundRoleWithGivenNameInDatabase() {
        // Given
        Role dummyRole = dataFactory.createAndSaveSingle();

        // When
        Role roleReturnedByRepository = roleRepository.findByName(dummyRole.getName());

        // Then
        assertThat(roleReturnedByRepository).isNotNull().isEqualTo(dummyRole);
    }

    @Test
    public void notFoundRoleWithGivenNameInDatabase() {
        // Given
        String nonExistingRoleName = "NO ROLE HERE";

        // When
        Role roleReturnedByRepository = roleRepository.findByName(nonExistingRoleName);

        // Then
        assertThat(roleReturnedByRepository).isNull();
    }
}