package pl.lodz.p.aurora.mentors.domain.dto;

import pl.lodz.p.aurora.skills.domain.dto.SkillBasicDto;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;

public class MentorDto {

    private Long id;
    private UserBasicDto user;
    private SkillBasicDto skill;
    private boolean approved = false;
    private boolean active = true;

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
