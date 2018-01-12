package pl.lodz.p.aurora.users.web.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.mentors.web.converter.MentorEntityToDtoConverter;
import pl.lodz.p.aurora.mentors.web.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.skills.web.converter.EvaluationEntityToDtoConverter;
import pl.lodz.p.aurora.skills.web.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.common.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=EMPLOYEE")
@RestController
public class UserEmployeeController extends BaseController {

    private final UserService service;
    private final EvaluationEntityToDtoConverter eEntityToDtoConverter = new EvaluationEntityToDtoConverter();
    private final MentorEntityToDtoConverter mEntityToDtoConverter = new MentorEntityToDtoConverter();

    @Autowired
    public UserEmployeeController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "{userId:[\\d]+}/evaluations/")
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<List<EvaluationDto>> findEmployeeEvaluations(@PathVariable Long userId) {
        List<Evaluation> employeeEvaluations = new ArrayList<>(service.findById(userId).getSkills());

        return ResponseEntity.ok()
                .body(employeeEvaluations.stream().map(eEntityToDtoConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "me/mentoring/")
    public ResponseEntity<List<MentorDto>> findEmployeeMentorActivity(@AuthenticationPrincipal User activeUser) {
        List<Evaluation> employeeEvaluations = new ArrayList<>(service.findById(activeUser.getId()).getSkills());
        List<Mentor> employeeMentorActivity = employeeEvaluations.stream().map(Evaluation::getMentor).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(employeeMentorActivity.stream().map(mEntityToDtoConverter::convert).collect(Collectors.toList()));
    }
}
