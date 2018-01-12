package pl.lodz.p.aurora.mme.web.controller.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.mme.web.converter.FeedbackEntityToDtoConverter;
import pl.lodz.p.aurora.mme.web.converter.MentorDtoToEntityConverter;
import pl.lodz.p.aurora.mme.web.converter.MentorEntityToDtoConverter;
import pl.lodz.p.aurora.mme.web.dto.FeedbackDto;
import pl.lodz.p.aurora.mme.web.dto.MentorDto;
import pl.lodz.p.aurora.mme.web.dto.MentorSearchDto;
import pl.lodz.p.aurora.mme.domain.entity.Feedback;
import pl.lodz.p.aurora.mme.domain.entity.Mentor;
import pl.lodz.p.aurora.mme.service.unitleader.MentorUnitLeaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/mentors/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class MentorUnitLeaderController extends BaseController {

    private final MentorUnitLeaderService service;
    private final MentorDtoToEntityConverter dtoToEntityConverter = new MentorDtoToEntityConverter();
    private final MentorEntityToDtoConverter entityToDtoConverter = new MentorEntityToDtoConverter();
    private final FeedbackEntityToDtoConverter fEntityToDtoConverter = new FeedbackEntityToDtoConverter ();

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
        return respondWithETag(service.findById(mentorId), entityToDtoConverter);
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

    @GetMapping(value = "{mentorId:[\\d]+}/feedback/")
    public ResponseEntity<List<FeedbackDto>> findMentorFeedback(@PathVariable Long mentorId) {
        Mentor requestedMentor = service.findById(mentorId);
        List<Feedback> feedback = new ArrayList<>(requestedMentor.getFeedback());

        return ResponseEntity.ok(feedback.stream().map(fEntityToDtoConverter::convert).collect(Collectors.toList()));
    }
}
