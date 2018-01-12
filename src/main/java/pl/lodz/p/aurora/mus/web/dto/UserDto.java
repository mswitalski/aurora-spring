package pl.lodz.p.aurora.mus.web.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.common.validator.NoHtml;
import pl.lodz.p.aurora.mus.domain.entity.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO class for User entity.
 */
public class UserDto implements Cloneable {

    private Long id;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Pattern(regexp = "[a-zA-Z0-9]*", message = "{User.username.Pattern}")
    @Size(min = 3, max = 20, message = "{Default.Size.MinMax}")
    private String username;

    @Email(message = "{Default.Email}")
    @NotEmpty(message = "{Default.NotEmpty}")
    @NotNull(message = "{Default.NotNull}")
    @Size(max = 40, message = "{Default.Size.Max}")
    private String email;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 20, message = "{Default.Size.MinMax}")
    private String name;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(min = 3, max = 30, message = "{Default.Size.MinMax}")
    private String surname;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(min = 2, max = 40, message = "{Default.Size.MinMax}")
    private String position;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(max = 200, message = "{Default.Size.Max}")
    private String goals = "";

    @NotNull(message = "{Default.NotNull}")
    private boolean enabled = true;

    private Set<Role> roles = new HashSet<>();
    private Set<DutyBasicDto> duties = new HashSet<>();

    public UserDto() {
    }

    public UserDto(Long id, String username, String email, String name, String surname, String position, String goals, boolean enabled, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.goals = goals;
        this.enabled = enabled;
        this.roles = new HashSet<>(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = new HashSet<>(roles);
    }

    public Set<DutyBasicDto> getDuties() {
        return new HashSet<>(this.duties);
    }

    public void setDuties(Set<DutyBasicDto> duties) {
        this.duties = new HashSet<>(duties);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public UserDto clone() {
        UserDto clonedUser = new UserDto();
        clonedUser.setId(this.id);
        clonedUser.setUsername(this.username);
        clonedUser.setEmail(this.email);
        clonedUser.setName(this.name);
        clonedUser.setSurname(this.surname);
        clonedUser.setPosition(this.position);
        clonedUser.setGoals(this.goals);
        clonedUser.setEnabled(this.enabled);
        clonedUser.setRoles(new HashSet<>(roles));

        return clonedUser;
    }
}
