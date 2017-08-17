package pl.lodz.p.aurora.domain.entity;

import javax.persistence.*;

/**
 * Entity class holding information about an user role in the system.
 */
@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @Column(name = "NAME", length = 10)
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
}
