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
import pl.lodz.p.aurora.skills.service.unitleader.SkillUnitLeaderService;

@RequestMapping(value = "api/v1/skills/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class SkillUnitLeaderController extends BaseController {
    
    private final SkillUnitLeaderService skillService;
    private final SkillEntityToDtoConverter entityToDtoConverter = new SkillEntityToDtoConverter();
    private final SkillDtoToEntityConverter dtoToEntityConverter = new SkillDtoToEntityConverter();

    @Autowired
    public SkillUnitLeaderController(SkillUnitLeaderService skillService) {
        this.skillService = skillService;
    }

    @GetMapping(value = "{skillId:[\\d]+}")
    public ResponseEntity<SkillDto> findById(@PathVariable Long skillId) {
        return respondWithConversion(skillService.findById(skillId), entityToDtoConverter);
    }

    @PostMapping
    public ResponseEntity<SkillDto> create(@Validated @RequestBody SkillDto formData) {
        Skill receivedUser = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(skillService.create(receivedUser)));
    }

    @DeleteMapping(value = "{skillId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long skillId, @RequestHeader("If-Match") String eTag) {
        skillService.delete(skillId, eTag);

        return ResponseEntity.noContent().build();
    }

//    @PutMapping(value = "{skillId:[\\d]+}")
//    public ResponseEntity<Void> update(@PathVariable Long skillId, @Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
//        skillService.update(userId, dtoToEntityConverter.convert(user), sanitizeReceivedETag(eTag));
//
//        return ResponseEntity.noContent().build();
//    }
}
