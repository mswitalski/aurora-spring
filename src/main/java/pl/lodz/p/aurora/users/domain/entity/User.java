package pl.lodz.p.aurora.users.domain.entity;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity class holding information about an user account.
 */
@Entity
@Table(name = "`user`", indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "surname"),
        @Index(columnList = "enabled")
})
public class User extends VersionedEntity implements Cloneable, UserDetails {

    @Id
    @GeneratedValue(generator = "user_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_pk_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 20, unique = true, updatable = false)
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @Column(nullable = false, length = 60)
    @NotNull
    @Size(min = 3, max = 60)
    private String password;

    @Column(nullable = false, length = 40, unique = true)
    @Email
    @NotNull
    @Size(max = 40)
    private String email;

    @Column(nullable = false, length = 20)
    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @Column(nullable = false, length = 30)
    @NotNull
    @Size(min = 3, max = 30)
    private String surname;

    @Column(nullable = false, length = 40)
    @NotNull
    @Size(min = 2, max = 40)
    private String position;

    @Column(nullable = false, length = 200)
    @NotNull
    @Size(max = 200)
    private String goals = "";

    @Column(nullable = false)
    @NotNull
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id", nullable = false, updatable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_name", referencedColumnName = "name", nullable = false, updatable = false
            )
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String password, String email, String name, String surname, String position, String goals, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.goals = goals;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles.isEmpty() || roles == null) {
            return Collections.emptySet();
        }

        return roles.stream()
                .map(Role::getName)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toList());
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

    public void assignRole(Role role) {
        this.roles.add(role);
    }

    public void retractRole(Role role) {
        this.roles.remove(role);
    }

    @Override
    public User clone() {
        User clonedUser = new User();
        clonedUser.setId(this.id);
        clonedUser.setUsername(this.username);
        clonedUser.setPassword(this.password);
        clonedUser.setEmail(this.email);
        clonedUser.setName(this.name);
        clonedUser.setSurname(this.surname);
        clonedUser.setPosition(this.position);
        clonedUser.setGoals(this.goals);
        clonedUser.setEnabled(this.enabled);
        clonedUser.setRoles(new HashSet<>(roles));
        clonedUser.setVersion(this.getVersion());

        return clonedUser;
    }
}
