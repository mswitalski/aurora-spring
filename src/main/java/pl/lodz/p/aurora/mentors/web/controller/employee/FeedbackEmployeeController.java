package pl.lodz.p.aurora.mentors.web.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.mentors.web.converter.FeedbackDtoToEntityConverter;
import pl.lodz.p.aurora.mentors.web.converter.FeedbackEntityToDtoConverter;
import pl.lodz.p.aurora.mentors.web.dto.FeedbackDto;
import pl.lodz.p.aurora.mentors.domain.entity.Feedback;
import pl.lodz.p.aurora.mentors.service.employee.FeedbackEmployeeService;
import pl.lodz.p.aurora.users.domain.entity.User;

@RequestMapping(value = "api/v1/feedback/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class FeedbackEmployeeController extends BaseController  {

    private final FeedbackEmployeeService service;
    private final FeedbackDtoToEntityConverter dtoToEntityConverter = new FeedbackDtoToEntityConverter();
    private final FeedbackEntityToDtoConverter entityToDtoConverter = new FeedbackEntityToDtoConverter();

    @Autowired
    public FeedbackEmployeeController(FeedbackEmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FeedbackDto> create(@RequestBody FeedbackDto formData, @AuthenticationPrincipal User activeUser) {
        Feedback receivedFeedback = dtoToEntityConverter.convert(formData);

        return respondWithETag(service.create(receivedFeedback, activeUser), entityToDtoConverter);
    }
}
