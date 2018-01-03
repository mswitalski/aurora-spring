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

    private final SkillService service;
    private final SkillBasicDtoConverter converter = new SkillBasicDtoConverter();

    @Autowired
    public SkillController(SkillService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SkillBasicDto>> findAll() {
        return ResponseEntity.ok().body(service.findAll().stream().map(converter::convert).collect(Collectors.toList()));
    }
}
