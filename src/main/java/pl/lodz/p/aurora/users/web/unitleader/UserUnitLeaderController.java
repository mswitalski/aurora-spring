package pl.lodz.p.aurora.users.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.skills.domain.converter.EvaluationEntityToDtoConverter;
import pl.lodz.p.aurora.skills.domain.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.users.domain.converter.UserDtoToEntityConverter;
import pl.lodz.p.aurora.users.domain.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.CreateUserFormDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.common.UserService;
import pl.lodz.p.aurora.users.service.unitleader.UserUnitLeaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class UserUnitLeaderController extends BaseController {

    private final UserUnitLeaderService userUnitLeaderService;
    private final UserService userService;
    private final UserEntityToDtoConverter entityToDtoConverter = new UserEntityToDtoConverter();
    private final UserDtoToEntityConverter dtoToEntityConverter = new UserDtoToEntityConverter();
    private final EvaluationEntityToDtoConverter evalEntityToDtoConverter = new EvaluationEntityToDtoConverter();

    @Autowired
    public UserUnitLeaderController(UserUnitLeaderService userUnitLeaderService, UserService userService) {
        this.userUnitLeaderService = userUnitLeaderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody CreateUserFormDto formData) {
        User receivedUser = dtoToEntityConverter.convert(formData);
        receivedUser.setPassword(formData.getPassword());

        return ResponseEntity.ok().body(entityToDtoConverter.convert(userUnitLeaderService.create(receivedUser)));
    }

    @DeleteMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestHeader("If-Match") String eTag) {
        userUnitLeaderService.delete(userId, eTag);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long userId, @Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
        userUnitLeaderService.update(userId, dtoToEntityConverter.convert(user), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}/duties/")
    public ResponseEntity<Void> updateDuties(@PathVariable Long userId, @Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
        userUnitLeaderService.updateDuties(userId, dtoToEntityConverter.convert(user), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{userId:[\\d]+}/evaluations/")
    public ResponseEntity<List<EvaluationDto>> findUserEvaluations(@PathVariable Long userId) {
        List<Evaluation> userEvaluations = new ArrayList<>(userService.findById(userId).getSkills());

        return ResponseEntity.ok()
                .body(userEvaluations.stream().map(evalEntityToDtoConverter::convert).collect(Collectors.toList()));
    }
}
