package pl.lodz.p.aurora.mus.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.mus.web.converter.UserDtoToEntityConverter;
import pl.lodz.p.aurora.mus.web.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.mus.web.dto.AdminPasswordChangeFormDto;
import pl.lodz.p.aurora.mus.web.dto.CreateUserFormDto;
import pl.lodz.p.aurora.mus.web.dto.UserDto;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.service.admin.UserAdminService;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=ADMIN")
@RestController
public class UserAdminController extends BaseController {

    private final UserAdminService service;
    private final UserEntityToDtoConverter entityToDtoConverter = new UserEntityToDtoConverter();
    private final UserDtoToEntityConverter dtoToEntityConverter = new UserDtoToEntityConverter();

    @Autowired
    public UserAdminController(UserAdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody CreateUserFormDto formData) {
        User receivedUser = dtoToEntityConverter.convert(formData);
        receivedUser.setPassword(formData.getPassword());

        return ResponseEntity.ok().body(entityToDtoConverter.convert(service.create(receivedUser)));
    }

    @DeleteMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestHeader("If-Match") String eTag) {
        service.delete(userId, eTag);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<Void> update(@PathVariable Long userId, @Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
        service.update(userId, dtoToEntityConverter.convert(user), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{userId:[\\d]+}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userId, @Validated @RequestBody AdminPasswordChangeFormDto formData, @RequestHeader("If-Match") String eTag) {
        service.updatePassword(userId, formData.getNewPassword(), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }
}
