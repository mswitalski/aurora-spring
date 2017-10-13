package pl.lodz.p.aurora.users.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.DutyDtoToEntityConverter;
import pl.lodz.p.aurora.users.domain.converter.DutyEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.DutyDto;
import pl.lodz.p.aurora.users.service.DutyService;

/**
 * REST controller for managing any requests related to user duties in the system.
 */
@RequestMapping("api/")
@RestController
public class DutyController extends BaseController {

    private final DutyService dutyService;
    private final DutyEntityToDtoConverter entityToDtoConverter;
    private final DutyDtoToEntityConverter dtoToEntityConverter;

    @Autowired
    public DutyController(DutyService dutyService,
                          DutyEntityToDtoConverter edConverter,
                          DutyDtoToEntityConverter deConverter) {
        this.dutyService = dutyService;
        this.entityToDtoConverter = edConverter;
        this.dtoToEntityConverter = deConverter;
    }

    @PostMapping(value = "admin/duties/")
    public ResponseEntity<DutyDto> createAsAdmin(@Validated @RequestBody DutyDto userDto) {
        DutyDto savedDuty = entityToDtoConverter
                .convert(dutyService.createAsAdmin(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedDuty);
    }

    @PostMapping(value = "unitleader/duties/")
    public ResponseEntity<DutyDto> createAsUnitLeader(@Validated @RequestBody DutyDto userDto) {
        DutyDto savedDuty = entityToDtoConverter
                .convert(dutyService.createAsUnitLeader(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedDuty);
    }

    @GetMapping(value = "duties/")
    public ResponseEntity<Page<DutyDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(dutyService.findAllByPage(pageable).map(entityToDtoConverter));
    }

    @GetMapping(value = "duties/{dutyId}")
    public ResponseEntity<DutyDto> findById(@PathVariable Long dutyId) {
        return ResponseEntity.ok().body(entityToDtoConverter.convert(dutyService.findById(dutyId)));
    }

    @PutMapping(value = "admin/duties/")
    public ResponseEntity<Void> updateAsAdmin(@RequestHeader("If-Match") String eTag, @Validated @RequestBody DutyDto duty) {
        dutyService.updateAsAdmin(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(duty));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "unitleader/duties/")
    public ResponseEntity<Void> updateAsUnitLeader(@RequestHeader("If-Match") String eTag, @Validated @RequestBody DutyDto duty) {
        dutyService.updateAsUnitLeader(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(duty));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "admin/duties/{dutyId}")
    public ResponseEntity<Void> deleteAsAdmin(@RequestHeader("If-Match") String eTag, @PathVariable Long dutyId) {
        dutyService.deleteAsAdmin(eTag, dutyId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "unitleader/duties/{dutyId}")
    public ResponseEntity<Void> deleteAsUnitLeader(@RequestHeader("If-Match") String eTag, @PathVariable Long dutyId) {
        dutyService.deleteAsUnitLeader(eTag, dutyId);

        return ResponseEntity.ok().build();
    }
}
