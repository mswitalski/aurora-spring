package pl.lodz.p.aurora.mentors.web.dto;

import pl.lodz.p.aurora.skills.web.dto.EvaluationDto;

public class MentorDto {

    private Long id;
    private EvaluationDto evaluation;
    private boolean approved = false;
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EvaluationDto getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationDto evaluation) {
        this.evaluation = evaluation;
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
