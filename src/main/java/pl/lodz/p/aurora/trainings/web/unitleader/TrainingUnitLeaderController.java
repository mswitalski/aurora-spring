package pl.lodz.p.aurora.trainings.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.trainings.domain.converter.TrainingBasicDtoConverter;
import pl.lodz.p.aurora.trainings.domain.converter.TrainingDtoToEntityConverter;
import pl.lodz.p.aurora.trainings.domain.converter.TrainingEntityToDtoConverter;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingBasicDto;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingDto;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingSearchDto;
import pl.lodz.p.aurora.trainings.domain.entity.Training;
import pl.lodz.p.aurora.trainings.service.unitleader.TrainingUnitLeaderService;

@RequestMapping(value = "api/v1/trainings/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class TrainingUnitLeaderController extends BaseController {

    private final TrainingUnitLeaderService service;
    private final TrainingEntityToDtoConverter entityToDtoConverter = new TrainingEntityToDtoConverter();
    private final TrainingDtoToEntityConverter dtoToEntityConverter = new TrainingDtoToEntityConverter();
    private final TrainingBasicDtoConverter basicConverter = new TrainingBasicDtoConverter();

    @Autowired
    public TrainingUnitLeaderController(TrainingUnitLeaderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TrainingDto> create(
            @Validated @RequestBody TrainingDto formData,
            @RequestHeader(name = "Outlook-Authorization", required = false) String outlookAuthToken
    ) {
        Training receivedSkill = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(service.create(receivedSkill, outlookAuthToken)));
    }

    @DeleteMapping(value = "{trainingId:[\\d]+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long trainingId,
            @RequestHeader("If-Match") String eTag,
            @RequestHeader(name = "Outlook-Authorization", required = false) String outlookAuthToken
    ) {
        service.delete(trainingId, eTag, outlookAuthToken);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<TrainingBasicDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok(service.findAllByPage(pageable).map(basicConverter));
    }

    @GetMapping(value = "{trainingId:[\\d]+}")
    public ResponseEntity<TrainingDto> findById(@PathVariable Long trainingId) {
        return respondWithETag(service.findById(trainingId), entityToDtoConverter);
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<TrainingBasicDto>> search(@RequestBody TrainingSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok(service.search(criteria, pageable).map(basicConverter));
    }

    @PutMapping(value = "{trainingId:[\\d]+}")
    public ResponseEntity<Void> update(
            @PathVariable Long trainingId,
            @Validated @RequestBody TrainingDto training,
            @RequestHeader("If-Match") String eTag,
            @RequestHeader(name = "Outlook-Authorization", required = false) String outlookAuthToken
    ) {
        service.update(trainingId, dtoToEntityConverter.convert(training), sanitizeReceivedETag(eTag), outlookAuthToken);

        return ResponseEntity.noContent().build();
    }
}