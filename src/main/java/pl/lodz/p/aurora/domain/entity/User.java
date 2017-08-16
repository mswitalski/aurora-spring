package pl.lodz.p.aurora.domain.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Entity class holding information about an user account.
 */
@Entity
@Table(name = "USER", indexes = {
        @Index(columnList = "NAME"),
        @Index(columnList = "SURNAME"),
        @Index(columnList = "IS_ENABLED")
})
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "user_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_pk_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 20, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;

    @Column(name = "EMAIL", nullable = false, length = 40, unique = true)
    private String email;

    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 30)
    private String surname;

    @Column(name = "POSITION", nullable = false, length = 40)
    private String position;

    @Column(name = "GOALS", nullable = false, length = 200)
    private String goals;

    @Column(name = "IS_ENABLED", nullable = false)
    private boolean enabled = true;

    @Version
    private Long version;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
