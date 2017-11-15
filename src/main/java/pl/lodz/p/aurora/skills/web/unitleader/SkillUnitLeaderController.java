package pl.lodz.p.aurora.skills.web.unitleader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.common.web.BaseController;

@RequestMapping(value = "api/v1/skills/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class SkillUnitLeaderController extends BaseController {
}
