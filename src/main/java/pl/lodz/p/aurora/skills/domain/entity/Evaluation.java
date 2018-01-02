package pl.lodz.p.aurora.skills.domain.entity;

import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.skills.domain.other.SkillLevel;
import pl.lodz.p.aurora.users.domain.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "skill_id"}, name = "unique_evaluation_pair")})
public class Evaluation extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "evaluation_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "evaluation_pk_sequence", sequenceName = "evaluation_id_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false, updatable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private SkillLevel selfEvaluation;

    @Enumerated(EnumType.STRING)
    private SkillLevel leaderEvaluation;

    @Column(nullable = false, length = 200)
    @NotNull
    @Size(max = 200)
    private String selfExplanation = "";

    @Column(nullable = false, length = 200)
    @NotNull
    @Size(max = 200)
    private String leaderExplanation = "";

    @OneToOne(mappedBy = "evaluation", cascade = CascadeType.REMOVE)
    private Mentor mentor;

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

    public SkillLevel getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(SkillLevel selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    public SkillLevel getLeaderEvaluation() {
        return leaderEvaluation;
    }

    public void setLeaderEvaluation(SkillLevel leaderEvaluation) {
        this.leaderEvaluation = leaderEvaluation;
    }

    public String getSelfExplanation() {
        return selfExplanation;
    }

    public void setSelfExplanation(String selfExplanation) {
        this.selfExplanation = selfExplanation;
    }

    public String getLeaderExplanation() {
        return leaderExplanation;
    }

    public void setLeaderExplanation(String leaderExplanation) {
        this.leaderExplanation = leaderExplanation;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
