package pl.lodz.p.aurora.users.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.DutyBasicDtoConverter;
import pl.lodz.p.aurora.users.domain.converter.DutyDtoToEntityConverter;
import pl.lodz.p.aurora.users.domain.converter.DutyEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyBasicDto;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.domain.dto.DutySearchDto;
import pl.lodz.p.aurora.users.service.unitleader.DutyUnitLeaderService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing any requests related to user duties in the system.
 */
@RequestMapping(value = "api/v1/duties/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class DutyUnitLeaderController extends BaseController {

    private final DutyUnitLeaderService dutyService;
    private final DutyEntityToDtoConverter entityToDtoConverter = new DutyEntityToDtoConverter();
    private final DutyDtoToEntityConverter dtoToEntityConverter = new DutyDtoToEntityConverter();
    private final DutyBasicDtoConverter basicConverter = new DutyBasicDtoConverter();

    @Autowired
    public DutyUnitLeaderController(DutyUnitLeaderService dutyService) {
        this.dutyService = dutyService;
    }

    @PostMapping
    public ResponseEntity<DutyDto> create(@Validated @RequestBody DutyDto userDto) {
        DutyDto savedDuty = entityToDtoConverter
                .convert(dutyService.create(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedDuty);
    }

    @DeleteMapping(value = "{dutyId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long dutyId, @RequestHeader("If-Match") String eTag) {
        dutyService.delete(dutyId, eTag);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<DutyBasicDto>> findAll() {
        return ResponseEntity.ok().body(dutyService.findAll().stream().map(basicConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<DutyBasicDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok().body(dutyService.findAllByPage(pageable).map(basicConverter));
    }

    @GetMapping(value = "{dutyId:[\\d]+}")
    public ResponseEntity<DutyDto> findById(@PathVariable Long dutyId) {
        return respondWithConversion(dutyService.findById(dutyId), entityToDtoConverter);
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<DutyBasicDto>> search(@RequestBody DutySearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(dutyService.search(criteria, pageable).map(basicConverter));
    }

    @PutMapping(value = "{dutyId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long dutyId, @Validated @RequestBody DutyDto duty, @RequestHeader("If-Match") String eTag) {
        dutyService.update(dutyId, dtoToEntityConverter.convert(duty), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
