package pl.lodz.p.aurora.skills.web.unitleader;

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
import pl.lodz.p.aurora.skills.service.unitleader.EvaluationUnitLeaderService;
import pl.lodz.p.aurora.users.domain.entity.User;

@RequestMapping(value = "api/v1/evaluations/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class EvaluationUnitLeaderController extends BaseController {

    private final EvaluationUnitLeaderService evaluationService;
    private final EvaluationDtoToEntityConverter dtoToEntityConverter = new EvaluationDtoToEntityConverter();
    private final EvaluationEntityToDtoConverter entityToDtoConverter = new EvaluationEntityToDtoConverter();

    @Autowired
    public EvaluationUnitLeaderController(EvaluationUnitLeaderService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    @PreAuthorize("#formData.user.username != principal.username")
    public ResponseEntity<EvaluationDto> create(@Validated @RequestBody EvaluationDto formData) {
        Evaluation receivedEvaluation = dtoToEntityConverter.convert(formData);

        return ResponseEntity.ok().body(entityToDtoConverter.convert(evaluationService.create(receivedEvaluation)));
    }

    @DeleteMapping(value = "{evaluationId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long evaluationId, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        evaluationService.delete(evaluationId, eTag, activeUser);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{evaluationId:[\\d]+}")
    public ResponseEntity<EvaluationDto> findById(@PathVariable Long evaluationId) {
        return respondWithETag(evaluationService.findById(evaluationId), entityToDtoConverter);
    }

    @PutMapping(value = "{evaluationId:[\\d]+}")
    @PreAuthorize("#evaluation.user.username != principal.username")
    public ResponseEntity<Void> update(@PathVariable Long evaluationId, @Validated @RequestBody EvaluationDto evaluation, @RequestHeader("If-Match") String eTag) {
        evaluationService.update(evaluationId, dtoToEntityConverter.convert(evaluation), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
