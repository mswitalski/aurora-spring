package pl.lodz.p.aurora.builder;

import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.msk.domain.entity.Skill;

import java.util.HashSet;
import java.util.Set;

public class SkillBuilder {

    private String name = "Default Name";
    private Set<Evaluation> users = new HashSet<>();
    private Long version = 0L;

    public SkillBuilder withVersion(Long version) {
        this.version = version;

        return this;
    }

    public Skill build() {
        Skill skill = new Skill(name, users);
        skill.setVersion(this.version);

        return skill;
    }
}
