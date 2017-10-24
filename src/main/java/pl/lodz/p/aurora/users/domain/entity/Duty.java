package pl.lodz.p.aurora.users.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "unique_duty_name")
})
public class Duty extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "duty_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "duty_pk_sequence", sequenceName = "duty_id_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotEmpty
    @NotNull
    @Size(max = 100)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "duties")
    private Set<User> users = new HashSet<>();

    public Duty() {
    }

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

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    public void setUsers(Set<User> users) {
        this.users = new HashSet<>(users);
    }
}
