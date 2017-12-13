package pl.lodz.p.aurora.mentors.domain.entity;

import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.skills.domain.entity.Skill;
import pl.lodz.p.aurora.users.domain.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Mentor extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "mentor_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "mentor_pk_sequence", sequenceName = "mentor_id_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false, updatable = false)
    private Skill skill;

    @Column(nullable = false)
    @NotNull
    private boolean approved;

    @Column(nullable = false)
    @NotNull
    private boolean active;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
