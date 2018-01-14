package pl.lodz.p.aurora.mus.domain.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.aurora.msh.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.msh.validator.NoHtml;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.mtr.domain.entity.Training;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "`user`",
        indexes = {
                @Index(columnList = "name", name = "index_user_name"),
                @Index(columnList = "surname", name = "index_user_surname"),
                @Index(columnList = "enabled", name = "index_user_enabled")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username", name = "unique_user_username"),
                @UniqueConstraint(columnNames = "email", name = "unique_user_email")
        }
)
public class User extends VersionedEntity implements Cloneable, UserDetails {

    @Id
    @GeneratedValue(generator = "user_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_pk_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 20, updatable = false)
    @NoHtml
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]*")
    @Size(min = 3, max = 20)
    private String username;

    @Column(nullable = false, length = 60)
    @NotNull
    @Size(min = 3, max = 60)
    private String password;

    @Column(nullable = false, length = 40)
    @Email
    @NotEmpty
    @NotNull
    @Size(max = 40)
    private String email;

    @Column(nullable = false, length = 20)
    @NoHtml
    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @Column(nullable = false, length = 30)
    @NoHtml
    @NotNull
    @Size(min = 3, max = 30)
    private String surname;

    @Column(nullable = false, length = 40)
    @NoHtml
    @NotNull
    @Size(min = 2, max = 40)
    private String position;

    @Column(nullable = false, length = 200)
    @NoHtml
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

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Duty.class)
    @JoinTable(
            name = "user_duty",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id", nullable = false, updatable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "duty_id", referencedColumnName = "id", nullable = false, updatable = false
            )
    )
    private Set<Duty> duties = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Evaluation> skills = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Feedback> feedback = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Training> trainings = new HashSet<>();

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

    public void setUsername(String username) {
        this.username = username;
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

    public Set<Duty> getDuties() {
        return new HashSet<>(duties);
    }

    public void setDuties(Set<Duty> duties) {
        this.duties = new HashSet<>(duties);
    }

    public void removeDuty(Duty duty) {
        this.duties.remove(duty);
    }

    public Set<Evaluation> getSkills() {
        return new HashSet<>(this.skills);
    }

    public void setSkills(Set<Evaluation> skills) {
        this.skills = new HashSet<>(skills);
    }

    public Set<Feedback> getFeedback() {
        return new HashSet<>(this.feedback);
    }

    public void setFeedback(Set<Feedback> feedback) {
        this.feedback = new HashSet<>(feedback);
    }

    public Set<Training> getTrainings() {
        return new HashSet<>(trainings);
    }

    public void setTrainings(Set<Training> trainings) {
        this.trainings = new HashSet<>(trainings);
    }

    public void removeTraining(Training training) {
        this.trainings.remove(training);
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
        clonedUser.setDuties(new HashSet<>(duties));
        clonedUser.setVersion(this.getVersion());

        return clonedUser;
    }

    @PreRemove
    private void preRemove() {
        this.trainings.forEach(training -> training.removeUser(this));
    }
}
