package pl.lodz.p.aurora.msk.web.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.msk.web.converter.EvaluationDtoToEntityConverter;
import pl.lodz.p.aurora.msk.web.converter.EvaluationEntityToDtoConverter;
import pl.lodz.p.aurora.msk.web.dto.EvaluationDto;
import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.msk.service.employee.EvaluationEmployeeService;
import pl.lodz.p.aurora.mus.domain.entity.User;

@RequestMapping(value = "api/v1/evaluations/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class EvaluationEmployeeController extends BaseController {

    private final EvaluationEmployeeService service;
    private final EvaluationDtoToEntityConverter dtoToEntityConverter = new EvaluationDtoToEntityConverter();
    private final EvaluationEntityToDtoConverter entityToDtoConverter = new EvaluationEntityToDtoConverter();

    @Autowired
    public EvaluationEmployeeController(EvaluationEmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EvaluationDto> create(@Validated @RequestBody EvaluationDto formData, @AuthenticationPrincipal User activeUser) {
        Evaluation receivedEvaluation = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(service.create(receivedEvaluation, activeUser)));
    }

    @DeleteMapping(value = "{evaluationId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long evaluationId, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.delete(evaluationId, activeUser, eTag);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{evaluationId:[\\d]+}")
    public ResponseEntity<EvaluationDto> findById(@PathVariable Long evaluationId, @AuthenticationPrincipal User activeUser) {
        return respondWithETag(service.findById(evaluationId, activeUser), entityToDtoConverter);
    }

    @PutMapping(value = "{evaluationId:[\\d]+}")
    @PreAuthorize("#evaluation.user.username == principal.username")
    public ResponseEntity<Void> update(@PathVariable Long evaluationId, @Validated @RequestBody EvaluationDto evaluation, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        service.update(evaluationId, dtoToEntityConverter.convert(evaluation), sanitizeReceivedETag(eTag), activeUser);

        return ResponseEntity.noContent().build();
    }
}
