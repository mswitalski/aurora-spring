package pl.lodz.p.aurora.mentors.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.mentors.domain.converter.MentorDtoToEntityConverter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.service.unitleader.MentorUnitLeaderService;

@RequestMapping(value = "api/v1/mentors/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class MentorUnitLeaderController extends BaseController {


    private final MentorUnitLeaderService service;
    private final MentorDtoToEntityConverter dtoToEntityConverter = new MentorDtoToEntityConverter();

    @Autowired
    public MentorUnitLeaderController(MentorUnitLeaderService service) {
        this.service = service;
    }

    @DeleteMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long mentorId, @RequestHeader("If-Match") String eTag) {
        service.delete(mentorId, eTag);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{mentorId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long mentorId, @RequestBody MentorDto mentor, @RequestHeader("If-Match") String eTag) {
        service.update(mentorId, dtoToEntityConverter.convert(mentor), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
