package pl.lodz.p.aurora.skills.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.skills.domain.converter.SkillDtoToEntityConverter;
import pl.lodz.p.aurora.skills.domain.converter.SkillEntityToDtoConverter;
import pl.lodz.p.aurora.skills.domain.dto.SkillDto;
import pl.lodz.p.aurora.skills.domain.entity.Skill;
import pl.lodz.p.aurora.skills.service.common.SkillService;
import pl.lodz.p.aurora.skills.service.unitleader.SkillUnitLeaderService;

@RequestMapping(value = "api/v1/skills/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class SkillUnitLeaderController extends BaseController {
    
    private final SkillUnitLeaderService skillUnitLeaderService;
    private final SkillService skillService;
    private final SkillEntityToDtoConverter entityToDtoConverter = new SkillEntityToDtoConverter();
    private final SkillDtoToEntityConverter dtoToEntityConverter = new SkillDtoToEntityConverter();

    @Autowired
    public SkillUnitLeaderController(SkillUnitLeaderService skillUnitLeaderService, SkillService skillService) {
        this.skillUnitLeaderService = skillUnitLeaderService;
        this.skillService = skillService;
    }

    @GetMapping(value = "{skillId:[\\d]+}")
    public ResponseEntity<SkillDto> findById(@PathVariable Long skillId) {
        return respondWithConversion(skillService.findById(skillId), entityToDtoConverter);
    }

    @PostMapping
    public ResponseEntity<SkillDto> create(@Validated @RequestBody SkillDto formData) {
        Skill receivedSkill = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(skillUnitLeaderService.create(receivedSkill)));
    }

    @DeleteMapping(value = "{skillId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long skillId, @RequestHeader("If-Match") String eTag) {
        skillUnitLeaderService.delete(skillId, eTag);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{skillId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long skillId, @Validated @RequestBody SkillDto skill, @RequestHeader("If-Match") String eTag) {
        skillUnitLeaderService.update(skillId, dtoToEntityConverter.convert(skill), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
