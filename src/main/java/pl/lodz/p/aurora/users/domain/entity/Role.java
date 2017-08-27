package pl.lodz.p.aurora.users.domain.entity;

import pl.lodz.p.aurora.common.domain.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Entity class holding information about an user role in the system.
 */
@Entity
@Table
public class Role extends BaseEntity {

    @Id
    @Column(length = 15)
    @Size(min = 2, max = 15)
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role[" +
                "name='" + name + '\'' +
                ']';
    }
}
