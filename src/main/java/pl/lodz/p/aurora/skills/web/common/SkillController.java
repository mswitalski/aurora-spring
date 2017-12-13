package pl.lodz.p.aurora.skills.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.skills.domain.converter.SkillBasicDtoConverter;
import pl.lodz.p.aurora.skills.domain.dto.SkillBasicDto;
import pl.lodz.p.aurora.skills.service.common.SkillService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/skills/", headers = "Requester-Role=ANY")
@RestController
public class SkillController {

    private final SkillService skillService;
    private final SkillBasicDtoConverter basicDtoConverter = new SkillBasicDtoConverter();

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillBasicDto>> findAll() {
        return ResponseEntity.ok().body(skillService.findAll().stream().map(basicDtoConverter::convert).collect(Collectors.toList()));
    }
}
