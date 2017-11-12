package pl.lodz.p.aurora.users.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.UserDtoToEntityConverter;
import pl.lodz.p.aurora.users.domain.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.AdminPasswordChangeFormDto;
import pl.lodz.p.aurora.users.domain.dto.CreateUserFormDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.service.admin.UserAdminService;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=ADMIN")
@RestController
public class UserAdminController extends BaseController {

    private final UserAdminService userAdminService;
    private final UserEntityToDtoConverter entityToDtoConverter;
    private final UserDtoToEntityConverter dtoToEntityConverter;

    @Autowired
    public UserAdminController(UserAdminService userAdminService, UserEntityToDtoConverter entityToDtoConverter, UserDtoToEntityConverter dtoToEntityConverter) {
        this.userAdminService = userAdminService;
        this.entityToDtoConverter = entityToDtoConverter;
        this.dtoToEntityConverter = dtoToEntityConverter;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody CreateUserFormDto userDto) {
        UserDto savedUser = entityToDtoConverter
                .convert(userAdminService.create(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedUser);
    }

    @PutMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> update(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userAdminService.update(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(user));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}/password")
    public ResponseEntity<UserDto> updatePassword(
            @RequestHeader("If-Match") String eTag, @PathVariable Long userId, @Validated @RequestBody AdminPasswordChangeFormDto formData
    ) {
        userAdminService.updatePassword(userId, formData.getNewPassword(), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
