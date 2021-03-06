package pl.lodz.p.aurora.mme.web.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.mme.web.converter.FeedbackEntityToDtoConverter;
import pl.lodz.p.aurora.mme.web.converter.MentorDtoToEntityConverter;
import pl.lodz.p.aurora.mme.web.converter.MentorEntityToDtoConverter;
import pl.lodz.p.aurora.mme.web.dto.FeedbackDto;
import pl.lodz.p.aurora.mme.web.dto.MentorDto;
import pl.lodz.p.aurora.mme.web.dto.MentorSearchDto;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;
import pl.lodz.p.aurora.mme.service.employee.MentorEmployeeService;
import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/mentors/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class MentorEmployeeController extends BaseController {

    private final MentorEmployeeService service;
    private final MentorDtoToEntityConverter dtoToEntityConverter = new MentorDtoToEntityConverter();
    private final MentorEntityToDtoConverter entityToDtoConverter = new MentorEntityToDtoConverter();
    private final FeedbackEntityToDtoConverter fEntityToDtoConverter = new FeedbackEntityToDtoConverter ();

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
    public ResponseEntity<MentorDto> findById(@PathVariable Long mentorId, @AuthenticationPrincipal User activeUser) {
        return respondWithETag(service.findById(mentorId, activeUser), entityToDtoConverter);
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<MentorDto>> search(@RequestBody MentorSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok(service.search(criteria, pageable).map(entityToDtoConverter));
    }

    @PutMapping(value = "{mentorId:[\\d]+}")
    @PreAuthorize("#mentor.evaluation.user.username == principal.username")
    public ResponseEntity<Void> update(@PathVariable Long mentorId, @RequestBody MentorDto mentor, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.update(mentorId, dtoToEntityConverter.convert(mentor), sanitizeReceivedETag(eTag), activeUser);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{mentorId:[\\d]+}/feedback/")
    public ResponseEntity<List<FeedbackDto>> findMentorFeedback(@PathVariable Long mentorId, @AuthenticationPrincipal User activeUser) {
        Mentor requestedMentor = service.findById(mentorId, activeUser);
        List<Feedback> feedback = new ArrayList<>(requestedMentor.getFeedback());

        return ResponseEntity.ok(feedback.stream().map(fEntityToDtoConverter::convert).collect(Collectors.toList()));
    }
}
