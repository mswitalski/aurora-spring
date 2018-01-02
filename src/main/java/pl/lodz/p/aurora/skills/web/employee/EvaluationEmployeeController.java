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
import pl.lodz.p.aurora.skills.service.employee.EvaluationEmployeeService;
import pl.lodz.p.aurora.users.domain.entity.User;

@RequestMapping(value = "api/v1/evaluations/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class EvaluationEmployeeController extends BaseController {

    private final EvaluationEmployeeService evaluationService;
    private final EvaluationDtoToEntityConverter dtoToEntityConverter = new EvaluationDtoToEntityConverter();
    private final EvaluationEntityToDtoConverter entityToDtoConverter = new EvaluationEntityToDtoConverter();

    @Autowired
    public EvaluationEmployeeController(EvaluationEmployeeService evaluationService) {
        this.evaluationService = evaluationService;
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
        return respondWithETag(evaluationService.findById(evaluationId, activeUser), entityToDtoConverter);
    }

    @PutMapping(value = "{evaluationId:[\\d]+}")
    @PreAuthorize("#evaluation.user.username == principal.username")
    public ResponseEntity<Void> update(@PathVariable Long evaluationId, @Validated @RequestBody EvaluationDto evaluation, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        evaluationService.update(evaluationId, dtoToEntityConverter.convert(evaluation), sanitizeReceivedETag(eTag), activeUser);

        return ResponseEntity.noContent().build();
    }
}
