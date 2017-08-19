package pl.lodz.p.aurora.users.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.repository.RoleRepository;
import pl.lodz.p.aurora.helper.RoleDataFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplUnitTests {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDataFactory dataFactory = new RoleDataFactory();
    private String fakeRoleName = "fakeRoleName";

    @Test
    public void emptyListReturnedIfNoRolesFound() {
        // Given
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<Role> returnedRolesList = roleService.findAll();

        // Then
        assertThat(returnedRolesList).isNotNull().isEmpty();
    }

    @Test
    public void oneRoleInListReturnedIfOneRoleFound() {
        // Given
        Integer howManyRoles = 1;
        when(roleRepository.findAll()).thenReturn(dataFactory.createMany(howManyRoles));

        // When
        List<Role> returnedRolesList = roleService.findAll();

        // Then
        assertThat(returnedRolesList).isNotNull().hasSize(howManyRoles);
    }

    @Test
    public void twoRolesInListReturnedIfTwoRolesFound() {
        // Given
        Integer howManyRoles = 2;
        when(roleRepository.findAll()).thenReturn(dataFactory.createMany(howManyRoles));

        // When
        List<Role> returnedRolesList = roleService.findAll();

        // Then
        assertThat(returnedRolesList).isNotNull().hasSize(howManyRoles);
    }

    @Test
    public void noRoleReturnedWithGivenName() {
        // Given
        when(roleRepository.findByName(anyString())).thenReturn(null);

        // When
        Role returnedRole = roleRepository.findByName(fakeRoleName);

        // Then
        assertThat(returnedRole).isNull();
    }

    @Test
    public void roleReturnedWithGivenName() {
        // Given
        Role dummyRole = dataFactory.createSingle();
        when(roleRepository.findByName(anyString())).thenReturn(dummyRole);

        // When
        Role returnedRole = roleRepository.findByName(fakeRoleName);

        // Then
        assertThat(returnedRole).isNotNull().isEqualTo(dummyRole);
    }
}