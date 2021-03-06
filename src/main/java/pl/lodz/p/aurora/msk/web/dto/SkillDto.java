package pl.lodz.p.aurora.msk.web.dto;

import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.msh.validator.NoHtml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SkillDto {

    private Long id;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @NotEmpty(message = "{Default.NotEmpty}")
    @Size(max = 50, message = "{Default.Size.Max}")
    private String name;

    public SkillDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
