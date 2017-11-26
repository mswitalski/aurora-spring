package pl.lodz.p.aurora.skills.web.unitleader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.common.web.BaseController;

@RequestMapping(value = "api/v1/skills/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class SkillUnitLeaderController extends BaseController {
    
//    private final SkillUnitLeaderService skillUnitLeaderService;
//
//    @Autowired
//    public SkillUnitLeaderController(SkillUnitLeaderService skillUnitLeaderService) {
//        this.skillUnitLeaderService = skillUnitLeaderService;
//    }
//
//    @PostMapping
//    public ResponseEntity<UserDto> create(@Validated @RequestBody SkillDto formData) {
//        User receivedUser = dtoToEntityConverter.convert(formData);
//        receivedUser.setPassword(formData.getPassword());
//
//        return ResponseEntity.ok().body(entityToDtoConverter.convert(skillUnitLeaderService.create(receivedUser)));
//    }
//
//    @DeleteMapping(value = "{skillId:[\\d]+}")
//    public ResponseEntity<Void> delete(@PathVariable Long skillId, @RequestHeader("If-Match") String eTag) {
//        skillUnitLeaderService.delete(userId, eTag);
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping(value = "{skillId:[\\d]+}")
//    public ResponseEntity<Void> update(@PathVariable Long skillId, @Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
//        skillUnitLeaderService.update(userId, dtoToEntityConverter.convert(user), sanitizeReceivedETag(eTag));
//
//        return ResponseEntity.noContent().build();
//    }
}
