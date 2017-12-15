package pl.lodz.p.aurora.mentors.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.mentors.domain.converter.MentorDtoToEntityConverter;
import pl.lodz.p.aurora.mentors.domain.converter.MentorEntityToDtoConverter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.dto.MentorSearchDto;
import pl.lodz.p.aurora.mentors.service.unitleader.MentorUnitLeaderService;

@RequestMapping(value = "api/v1/mentors/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class MentorUnitLeaderController extends BaseController {


    private final MentorUnitLeaderService service;
    private final MentorDtoToEntityConverter dtoToEntityConverter = new MentorDtoToEntityConverter();
    private final MentorEntityToDtoConverter entityToDtoConverter = new MentorEntityToDtoConverter();

    @Autowired
    public MentorUnitLeaderController(MentorUnitLeaderService service) {
        this.service = service;
    }

    @DeleteMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long mentorId, @RequestHeader("If-Match") String eTag) {
        service.delete(mentorId, eTag);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<MentorDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok(service.findAllByPage(pageable).map(entityToDtoConverter));
    }

    @GetMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<MentorDto> findById(@PathVariable Long mentorId) {
        return respondWithConversion(service.findById(mentorId), entityToDtoConverter);
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<MentorDto>> search(@RequestBody MentorSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok(service.search(criteria, pageable).map(entityToDtoConverter));
    }

    @PutMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long mentorId, @RequestBody MentorDto mentor, @RequestHeader("If-Match") String eTag) {
        service.update(mentorId, dtoToEntityConverter.convert(mentor), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
