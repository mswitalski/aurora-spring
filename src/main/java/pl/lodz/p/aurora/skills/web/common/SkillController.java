package pl.lodz.p.aurora.skills.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.skills.domain.converter.SkillBasicDtoConverter;
import pl.lodz.p.aurora.skills.domain.dto.SkillBasicDto;
import pl.lodz.p.aurora.skills.domain.dto.SkillSearchDto;
import pl.lodz.p.aurora.skills.service.common.SkillService;

@RequestMapping(value = "api/v1/skills/", headers = "Requester-Role=ANY")
@RestController
public class SkillController {

    private final SkillService skillService;
    private final SkillBasicDtoConverter basicDtoConverter;

    @Autowired
    public SkillController(SkillService skillService, SkillBasicDtoConverter basicDtoConverter) {
        this.skillService = skillService;
        this.basicDtoConverter = basicDtoConverter;
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<SkillBasicDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok().body(skillService.findAllByPage(pageable).map(basicDtoConverter));
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<SkillBasicDto>> searchForUsers(@RequestBody SkillSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(skillService.search(criteria, pageable).map(basicDtoConverter));
    }
}
