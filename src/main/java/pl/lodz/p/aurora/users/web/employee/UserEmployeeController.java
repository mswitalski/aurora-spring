package pl.lodz.p.aurora.users.web.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.skills.domain.converter.EvaluationEntityToDtoConverter;
import pl.lodz.p.aurora.skills.domain.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.users.service.common.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class UserEmployeeController extends BaseController {

    private final UserService userService;
    private final EvaluationEntityToDtoConverter entityToDtoConverter = new EvaluationEntityToDtoConverter();

    @Autowired
    public UserEmployeeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{userId:[\\d]+}/evaluations/")
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<List<EvaluationDto>> findEmployeeEvaluations(@PathVariable Long userId) {
        List<Evaluation> employeeEvaluations = new ArrayList<>(userService.findById(userId).getSkills());

        return ResponseEntity.ok()
                .body(employeeEvaluations.stream().map(entityToDtoConverter::convert).collect(Collectors.toList()));
    }
}
