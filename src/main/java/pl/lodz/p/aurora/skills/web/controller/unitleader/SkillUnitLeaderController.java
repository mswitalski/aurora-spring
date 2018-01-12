package pl.lodz.p.aurora.skills.web.controller.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.skills.web.converter.SkillBasicDtoConverter;
import pl.lodz.p.aurora.skills.web.converter.SkillDtoToEntityConverter;
import pl.lodz.p.aurora.skills.web.converter.SkillEntityToDtoConverter;
import pl.lodz.p.aurora.skills.web.dto.SkillBasicDto;
import pl.lodz.p.aurora.skills.web.dto.SkillDto;
import pl.lodz.p.aurora.skills.web.dto.SkillSearchDto;
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
    private final SkillBasicDtoConverter basicDtoConverter = new SkillBasicDtoConverter();

    @Autowired
    public SkillUnitLeaderController(SkillUnitLeaderService skillUnitLeaderService, SkillService skillService) {
        this.skillUnitLeaderService = skillUnitLeaderService;
        this.skillService = skillService;
    }

    @GetMapping(value = "{skillId:[\\d]+}")
    public ResponseEntity<SkillDto> findById(@PathVariable Long skillId) {
        return respondWithETag(skillService.findById(skillId), entityToDtoConverter);
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

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<SkillBasicDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok().body(skillUnitLeaderService.findAllByPage(pageable).map(basicDtoConverter));
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<SkillBasicDto>> search(@RequestBody SkillSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(skillUnitLeaderService.search(criteria, pageable).map(basicDtoConverter));
    }
}
