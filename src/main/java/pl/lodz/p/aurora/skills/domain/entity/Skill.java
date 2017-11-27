package pl.lodz.p.aurora.skills.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(indexes = {
            @Index(columnList = "name")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "name", name = "unique_skill_name")
        }
)
public class Skill extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "skill_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "skill_pk_sequence", sequenceName = "skill_id_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 20)
    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String name;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.REMOVE)
    private Set<Evaluation> users = new HashSet<>();

    public Skill() {
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

    public Set<Evaluation> getUsers() {
        return users;
    }

    public void setUsers(Set<Evaluation> users) {
        this.users = users;
    }
}
