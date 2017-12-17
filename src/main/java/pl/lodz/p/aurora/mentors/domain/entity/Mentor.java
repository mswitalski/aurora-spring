package pl.lodz.p.aurora.mentors.domain.entity;

import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"evaluation_id"}, name = "unique_mentor_evaluation")})
public class Mentor extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "mentor_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "mentor_pk_sequence", sequenceName = "mentor_id_sequence", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "evaluation_id", nullable = false, updatable = false)
    private Evaluation evaluation;

    @Column(nullable = false)
    @NotNull
    private boolean approved;

    @Column(nullable = false)
    @NotNull
    private boolean active;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.REMOVE)
    private Set<Feedback> feedback = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Set<Feedback> getFeedback() {
        return new HashSet<>(this.feedback);
    }

    public void setFeedback(Set<Feedback> feedback) {
        this.feedback = new HashSet<>(feedback);
    }
}
