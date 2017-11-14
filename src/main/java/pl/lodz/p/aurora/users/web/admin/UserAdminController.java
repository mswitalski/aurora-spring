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
import pl.lodz.p.aurora.users.domain.entity.User;
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
    public ResponseEntity<UserDto> create(@Validated @RequestBody CreateUserFormDto formData) {
        User receivedUser = dtoToEntityConverter.convert(formData);
        receivedUser.setPassword(formData.getPassword());

        return ResponseEntity.ok().body(entityToDtoConverter.convert(userAdminService.create(receivedUser)));
    }

    @DeleteMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestHeader("If-Match") String eTag) {
        userAdminService.delete(userId, eTag);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long userId, @Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
        userAdminService.update(userId, dtoToEntityConverter.convert(user), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userId, @Validated @RequestBody AdminPasswordChangeFormDto formData, @RequestHeader("If-Match") String eTag) {
        userAdminService.updatePassword(userId, formData.getNewPassword(), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
