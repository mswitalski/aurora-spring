package pl.lodz.p.aurora.mentors.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.mentors.domain.converter.MentorEntityToDtoConverter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.dto.MentorSearchDto;
import pl.lodz.p.aurora.mentors.service.common.MentorService;

@RequestMapping(value = "api/v1/mentors/", headers = "Requester-Role=ANY")
@RestController
public class MentorController extends BaseController {

    private final MentorService service;
    private final MentorEntityToDtoConverter converter = new MentorEntityToDtoConverter();

    @Autowired
    public MentorController(MentorService service) {
        this.service = service;
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<MentorDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok(service.findAllByPage(pageable).map(converter));
    }

    @GetMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<MentorDto> findById(@PathVariable Long mentorId) {
        return respondWithConversion(service.findById(mentorId), converter);
    }

    @GetMapping(value = "search/")
    public ResponseEntity<Page<MentorDto>> search(@RequestBody MentorSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok(service.search(criteria, pageable).map(converter));
    }
}
