package pl.lodz.p.aurora.skills.web.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.skills.domain.converter.EvaluationDtoToEntityConverter;
import pl.lodz.p.aurora.skills.domain.converter.EvaluationEntityToDtoConverter;
import pl.lodz.p.aurora.skills.domain.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.skills.service.employee.EvaluationService;
import pl.lodz.p.aurora.users.domain.entity.User;

@RequestMapping(value = "api/v1/evaluations/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class EvaluationEmployeeController extends BaseController {

    private final EvaluationService evaluationService;
    private final EvaluationDtoToEntityConverter dtoToEntityConverter;
    private final EvaluationEntityToDtoConverter entityToDtoConverter;

    @Autowired
    public EvaluationEmployeeController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
        this.dtoToEntityConverter = new EvaluationDtoToEntityConverter();
        this.entityToDtoConverter = new EvaluationEntityToDtoConverter();
    }

    @PostMapping
    public ResponseEntity<EvaluationDto> create(@Validated @RequestBody EvaluationDto formData, @AuthenticationPrincipal User activeUser) {
        Evaluation receivedEvaluation = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(evaluationService.create(receivedEvaluation, activeUser)));
    }

    @DeleteMapping(value = "{evaluationId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long evaluationId, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        evaluationService.delete(evaluationId, activeUser, eTag);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{evaluationId:[\\d]+}")
    public ResponseEntity<EvaluationDto> findById(@PathVariable Long evaluationId, @AuthenticationPrincipal User activeUser) {
        return respondWithConversion(evaluationService.findById(evaluationId, activeUser), entityToDtoConverter);
    }

    @PutMapping(value = "{evaluationId:[\\d]+}")
    @PreAuthorize("#evaluation.user.username == principal.username")
    public ResponseEntity<Void> update(@PathVariable Long evaluationId, @Validated @RequestBody EvaluationDto evaluation, @RequestHeader("If-Match") String eTag) {
        evaluationService.update(evaluationId, dtoToEntityConverter.convert(evaluation), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
