package pl.lodz.p.aurora.mtr.web.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.mtr.web.converter.TrainingBasicDtoConverter;
import pl.lodz.p.aurora.mtr.web.converter.TrainingEntityToDtoConverter;
import pl.lodz.p.aurora.mtr.web.dto.TrainingBasicDto;
import pl.lodz.p.aurora.mtr.web.dto.TrainingDto;
import pl.lodz.p.aurora.mtr.service.employee.TrainingEmployeeService;
import pl.lodz.p.aurora.mus.domain.entity.User;

@RequestMapping(value = "api/v1/trainings/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class TrainingEmployeeController extends BaseController {

    private final TrainingEmployeeService service;
    private final TrainingBasicDtoConverter basicConverter = new TrainingBasicDtoConverter();
    private final TrainingEntityToDtoConverter entityToDtoConverter = new TrainingEntityToDtoConverter();

    @Autowired
    public TrainingEmployeeController(TrainingEmployeeService service) {
        this.service = service;
    }

    @GetMapping(value = "finished/")
    public ResponseEntity<Page<TrainingBasicDto>> findAllFinished(@AuthenticationPrincipal User activeUser, Pageable pageable) {
        return ResponseEntity.ok(service.findAllFinished(activeUser, pageable).map(basicConverter));
    }

    @GetMapping(value = "planned/")
    public ResponseEntity<Page<TrainingBasicDto>> findAllPlanned(@AuthenticationPrincipal User activeUser, Pageable pageable) {
        return ResponseEntity.ok(service.findAllPlanned(activeUser, pageable).map(basicConverter));
    }

    @GetMapping(value = "{trainingId:[\\d]+}")
    public ResponseEntity<TrainingDto> findById(@PathVariable Long trainingId, @AuthenticationPrincipal User activeUser) {
        return respondWithETag(service.findById(trainingId, activeUser), entityToDtoConverter);
    }
}