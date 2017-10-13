package pl.lodz.p.aurora.users.domain.dto;

import pl.lodz.p.aurora.users.domain.entity.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class CreateUserFormDto extends UserDto {

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String password;

    public CreateUserFormDto() {
    }

    public CreateUserFormDto(Long id, String username, String email, String name, String surname, String position, String goals, boolean enabled, Set<Role> roles, String password) {
        super(id, username, email, name, surname, position, goals, enabled, roles);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
