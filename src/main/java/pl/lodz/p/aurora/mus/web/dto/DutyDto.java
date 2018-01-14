package pl.lodz.p.aurora.mus.web.dto;

import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.msh.validator.NoHtml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class DutyDto {

    private Long id;

    @NoHtml
    @NotEmpty(message = "{Default.NotEmpty}")
    @NotNull(message = "{Default.NotNull}")
    @Size(max = 100, message = "{Default.Size.Max}")
    private String name;

    private Set<UserDto> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDto> getUsers() {
        return new HashSet<>(users);
    }

    public void setUsers(Set<UserDto> users) {
        this.users = new HashSet<>(users);
    }
}
