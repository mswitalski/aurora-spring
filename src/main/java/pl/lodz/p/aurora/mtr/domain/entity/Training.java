package pl.lodz.p.aurora.mtr.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.msh.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.msh.validator.NoHtml;
import pl.lodz.p.aurora.mus.domain.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(columnList = "name", name = "index_training_name"),
        @Index(columnList = "type", name = "index_training_type"),
        @Index(columnList = "location", name = "index_training_location"),
})
public class Training extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "training_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "training_pk_sequence", sequenceName = "training_id_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    @NoHtml
    @NotNull
    @NotEmpty
    @Size(max = 100)
    private String name;

    @Column(nullable = false, length = 20)
    @NoHtml
    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String type;

    @Column(nullable = false, length = 50)
    @NoHtml
    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String location;

    @Column
    @NotNull
    private LocalDateTime startDateTime;

    @Column
    @NotNull
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    @NotNull
    private boolean internal = true;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer participantsLimit;

    @Column(nullable = false, length = 500)
    @NoHtml
    @NotNull
    @NotEmpty
    @Size(max = 500)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinTable(
            name = "user_training",
            joinColumns = @JoinColumn(
                    name = "training_id", referencedColumnName = "id", nullable = false, updatable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id", nullable = false, updatable = false
            )
    )
    private Set<User> users = new HashSet<>();

    @Override
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public Integer getParticipantsLimit() {
        return participantsLimit;
    }

    public void setParticipantsLimit(Integer participantsLimit) {
        this.participantsLimit = participantsLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    public void setUsers(Set<User> users) {
        this.users = new HashSet<>(users);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    @PreRemove
    private void preRemove() {
        this.users.forEach(user -> user.removeTraining(this));
    }
}
