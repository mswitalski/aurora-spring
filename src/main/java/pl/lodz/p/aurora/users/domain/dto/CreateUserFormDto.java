package pl.lodz.p.aurora.users.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserFormDto extends UserDto {

    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 60, message = "{Default.Size.MinMax}")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
