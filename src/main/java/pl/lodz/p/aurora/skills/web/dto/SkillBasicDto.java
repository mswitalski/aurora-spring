package pl.lodz.p.aurora.skills.web.dto;

public class SkillBasicDto {

    private Long id;
    private String name;

    public SkillBasicDto() {
    }

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
}
