package pl.lodz.p.aurora.users.web.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.UserDtoToEntityConverter;
import pl.lodz.p.aurora.users.domain.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.CreateUserFormDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.unitleader.UserUnitLeaderService;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=UNIT_LEADER")
@RestController
public class UserUnitLeaderController extends BaseController {

    private final UserUnitLeaderService userUnitLeaderService;
    private final UserEntityToDtoConverter entityToDtoConverter;
    private final UserDtoToEntityConverter dtoToEntityConverter;

    @Autowired
    public UserUnitLeaderController(UserUnitLeaderService userUnitLeaderService, UserEntityToDtoConverter entityToDtoConverter, UserDtoToEntityConverter dtoToEntityConverter) {
        this.userUnitLeaderService = userUnitLeaderService;
        this.entityToDtoConverter = entityToDtoConverter;
        this.dtoToEntityConverter = dtoToEntityConverter;
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
}
