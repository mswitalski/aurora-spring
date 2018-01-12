package pl.lodz.p.aurora.mme.web.controller.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.mme.service.unitleader.FeedbackUnitLeaderService;

@RequestMapping(value = "api/v1/feedback/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class FeedbackUnitLeaderController extends BaseController {

    private final FeedbackUnitLeaderService service;

    @Autowired
    public FeedbackUnitLeaderController(FeedbackUnitLeaderService service) {
        this.service = service;
    }

    @DeleteMapping(value = "{feedbackId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long feedbackId) {
        service.delete(feedbackId);

        return ResponseEntity.noContent().build();
    }
}
