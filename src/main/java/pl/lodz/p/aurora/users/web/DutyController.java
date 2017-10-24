package pl.lodz.p.aurora.users.web;

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
    private final DutyBasicDtoConverter basicConverter;

    @Autowired
    public DutyController(DutyService dutyService,
                          DutyEntityToDtoConverter edConverter,
                          DutyDtoToEntityConverter deConverter,
                          DutyBasicDtoConverter basicConverter) {
        this.dutyService = dutyService;
        this.entityToDtoConverter = edConverter;
        this.dtoToEntityConverter = deConverter;
        this.basicConverter = basicConverter;
    }

    @PostMapping(value = "unitleader/duties/")
    public ResponseEntity<DutyDto> create(@Validated @RequestBody DutyDto userDto) {
        DutyDto savedDuty = entityToDtoConverter
                .convert(dutyService.create(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedDuty);
    }

    @GetMapping(value = "duties/")
    public ResponseEntity<Page<DutyBasicDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(dutyService.findAllByPage(pageable).map(basicConverter));
    }

    @GetMapping(value = "duties/{dutyId}")
    public ResponseEntity<DutyDto> findById(@PathVariable Long dutyId) {
        return ResponseEntity.ok().body(entityToDtoConverter.convert(dutyService.findById(dutyId)));
    }

    @PutMapping(value = "unitleader/duties/")
    public ResponseEntity<Void> update(@RequestHeader("If-Match") String eTag, @Validated @RequestBody DutyDto duty) {
        dutyService.update(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(duty));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "unitleader/duties/{dutyId}")
    public ResponseEntity<Void> delete(@RequestHeader("If-Match") String eTag, @PathVariable Long dutyId) {
        dutyService.delete(eTag, dutyId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "search/duties/")
    public ResponseEntity<Page<DutyBasicDto>> search(@RequestBody DutySearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(dutyService.search(criteria, pageable).map(basicConverter));
    }
}
