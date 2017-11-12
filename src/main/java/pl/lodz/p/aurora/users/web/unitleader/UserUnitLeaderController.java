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
    public ResponseEntity<UserDto> create(@Validated @RequestBody CreateUserFormDto userDto) {
        UserDto savedUser = entityToDtoConverter
                .convert(userUnitLeaderService.create(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedUser);
    }

    @PutMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> update(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userUnitLeaderService.update(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(user));

        return ResponseEntity.noContent().build();
    }
}
