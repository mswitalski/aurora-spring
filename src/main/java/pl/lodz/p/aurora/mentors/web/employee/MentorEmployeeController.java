package pl.lodz.p.aurora.mentors.web.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.mentors.domain.converter.MentorDtoToEntityConverter;
import pl.lodz.p.aurora.mentors.domain.converter.MentorEntityToDtoConverter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.dto.MentorSearchDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.mentors.service.employee.MentorEmployeeService;
import pl.lodz.p.aurora.users.domain.entity.User;

@RequestMapping(value = "api/v1/mentors/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class MentorEmployeeController extends BaseController {

    private final MentorEmployeeService service;
    private final MentorDtoToEntityConverter dtoToEntityConverter = new MentorDtoToEntityConverter();
    private final MentorEntityToDtoConverter entityToDtoConverter = new MentorEntityToDtoConverter();

    @Autowired
    public MentorEmployeeController(MentorEmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MentorDto> create(@RequestBody MentorDto formData, @AuthenticationPrincipal User activeUser) {
        Mentor receivedEvaluation = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(service.create(receivedEvaluation, activeUser)));
    }

    @DeleteMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long mentorId, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.delete(mentorId, activeUser, eTag);

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

    @GetMapping(value = "search/")
    public ResponseEntity<Page<MentorDto>> search(@RequestBody MentorSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok(service.search(criteria, pageable).map(entityToDtoConverter));
    }

    @PutMapping(value = "{mentorId:[\\d]+}")
    @PreAuthorize("#mentor.user.username == principal.username")
    public ResponseEntity<Void> update(@PathVariable Long mentorId, @RequestBody MentorDto mentor, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.update(mentorId, dtoToEntityConverter.convert(mentor), sanitizeReceivedETag(eTag), activeUser);

        return ResponseEntity.noContent().build();
    }
}
