package pl.lodz.p.aurora.skills.domain.dto;

import pl.lodz.p.aurora.common.validator.NoHtml;
import pl.lodz.p.aurora.skills.domain.other.SkillLevel;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EvaluationDto {

    private Long id;
    private UserBasicDto user;
    private SkillBasicDto skill;
    private SkillLevel selfEvaluation;
    private SkillLevel leaderEvaluation;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(max = 200, message = "{Default.Size.Max}")
    private String selfExplanation = "";

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(max = 200, message = "{Default.Size.Max}")
    private String leaderExplanation = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserBasicDto getUser() {
        return user;
    }

    public void setUser(UserBasicDto user) {
        this.user = user;
    }

    public SkillBasicDto getSkill() {
        return skill;
    }

    public void setSkill(SkillBasicDto skill) {
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
}
