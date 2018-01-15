package pl.lodz.p.aurora.mus.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Entity class holding information about an user role in the system.
 */
@Entity
public class Role {

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

    @Override
    public int hashCode() {
        return 117 * name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role that = (Role) o;

        return name.equals(that.getName());
    }
}
