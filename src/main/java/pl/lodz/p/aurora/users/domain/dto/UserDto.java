package pl.lodz.p.aurora.users.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import pl.lodz.p.aurora.users.domain.entity.Role;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO class for User entity.
 */
public class UserDto {

    private Long id;

    @NotNull(message = "{UserDto.username.NotNull}")
    @Size(min = 3, max = 20, message = "{UserDto.username.Size}")
    private String username;

    @NotNull(message = "{UserDto.password.NotNull}")
    @Size(min = 3, max = 60, message = "{UserDto.password.Size}")
    private String password;

    @NotNull(message = "{UserDto.email.NotNull}")
    @Email(message = "{UserDto.email.Email}")
    @Size(max = 40, message = "{UserDto.email.Size}")
    private String email;

    @NotNull(message = "{UserDto.name.NotNull}")
    @Size(min = 3, max = 20, message = "{UserDto.name.Size}")
    private String name;

    @NotNull(message = "{UserDto.surname.NotNull}")
    @Size(min = 3, max = 30, message = "{UserDto.surname.Size}")
    private String surname;

    @NotNull(message = "{UserDto.position.NotNull}")
    @Size(min = 2, max = 40, message = "{UserDto.position.Size}")
    private String position;

    @Size(max = 200, message = "{UserDto.goals.Size}")
    private String goals = "";

    @NotNull(message = "{UserDto.enabled.NotNull}")
    private boolean enabled = true;

    private Set<Role> roles = new HashSet<>();

    public UserDto() {
    }

    public UserDto(Long id, String username, String password, String email, String name, String surname, String position, String goals, boolean enabled, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
