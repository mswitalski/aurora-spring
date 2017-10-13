package pl.lodz.p.aurora.users.domain.entity;

import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Duty extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "duty_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "duty_pk_sequence", sequenceName = "duty_id_sequence", allocationSize = 1)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "duties")
    private Set<User> users = new HashSet<>();

    public Duty() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
