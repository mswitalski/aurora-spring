package pl.lodz.p.aurora.users.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.domain.dto.ValidationMessageDto;
import pl.lodz.p.aurora.common.util.Translator;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.UserBasicDtoConverter;
import pl.lodz.p.aurora.users.domain.converter.UserDtoToEntityConverter;
import pl.lodz.p.aurora.users.domain.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.*;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * REST controller for managing any requests related to users account.
 */
@RequestMapping("api/")
@RestController
public class UserController extends BaseController {

    private final UserServiceImpl userService;
    private final Translator translator;
    private final UserEntityToDtoConverter entityToDtoConverter;
    private final UserDtoToEntityConverter dtoToEntityConverter;
    private final UserBasicDtoConverter basicConverter;

    @Autowired
    public UserController(UserServiceImpl userService,
                          Translator translator,
                          UserEntityToDtoConverter edConverter,
                          UserDtoToEntityConverter deConverter,
                          UserBasicDtoConverter basicConverter) {
        this.userService = userService;
        this.translator = translator;
        this.entityToDtoConverter = edConverter;
        this.dtoToEntityConverter = deConverter;
        this.basicConverter = basicConverter;
    }

    @PostMapping(value = "admin/users/")
    public ResponseEntity<UserDto> createAsAdmin(@Validated @RequestBody CreateUserFormDto userDto) {
        UserDto savedUser = entityToDtoConverter
                .convert(userService.createAsAdmin(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedUser);
    }

    @PostMapping(value = "unitleader/users/")
    public ResponseEntity<UserDto> createAsUnitLeader(@Validated @RequestBody CreateUserFormDto userDto) {
        UserDto savedUser = entityToDtoConverter
                .convert(userService.createAsUnitLeader(dtoToEntityConverter.convert(userDto)));

        return ResponseEntity.ok().body(savedUser);
    }

    @GetMapping(value = "users/")
    public ResponseEntity<Page<UserBasicDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAllByPage(pageable).map(basicConverter));
    }

    @GetMapping(value = "user")
    public ResponseEntity<UserDto> findLoggedUser() {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return respondWithUserDto(userService.findByUsername(loggedUser.getUsername()));
    }

    @GetMapping(value = "users/{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        return respondWithUserDto(userService.findByUsername(username));
    }

    private ResponseEntity<UserDto> respondWithUserDto(User user) {
        return ResponseEntity.ok().eTag(Long.toString(user.getVersion())).body(entityToDtoConverter.convert(user));
    }

    @PutMapping(value = "admin/users/")
    public ResponseEntity<Void> updateAsAdmin(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userService.updateOtherAccount(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(user));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "unitleader/users/")
    public ResponseEntity<Void> updateAsUnitLeader(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userService.updateOtherAccount(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(user));

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#user.getUsername() == principal.username")
    @PutMapping(value = "users/")
    public ResponseEntity<Void> updateOwnAccount(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userService.updateOwnAccount(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(user));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "admin/users/{userId}/password")
    public ResponseEntity<UserDto> updatePasswordAsAdmin(
            @RequestHeader("If-Match") String eTag, @PathVariable Long userId, @Validated @RequestBody AdminPasswordChangeFormDto formData
    ) {
        userService.updatePasswordAsAdmin(userId, formData.getNewPassword(), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "users/password")
    public ResponseEntity<Object> updateOwnPassword(
            @RequestHeader("If-Match") String eTag, @Validated @RequestBody PasswordChangeFormDto formData
    ) {
        Locale locale = LocaleContextHolder.getLocale();

        if (!formData.getNewPassword().equals(formData.getNewPasswordRepeated())) {
            return preparePasswordChangeErrorResponse("PasswordChangeFormDto.newPasswordRepeated.Invalid", "new-password-repeated", locale);
        }

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (wasUserPasswordUpdated(eTag, loggedUser.getUsername(), formData)) {
            userService.findByUsername(loggedUser.getUsername());

            return ResponseEntity.noContent().build();

        } else {
            return preparePasswordChangeErrorResponse("PasswordChangeFormDto.currentPassword.Invalid", "current-password", locale);
        }
    }

    private ResponseEntity<Object> preparePasswordChangeErrorResponse(String translationKey, String fieldName, Locale locale) {
        List<ValidationMessageDto> errorMessage =
                Collections.singletonList(new ValidationMessageDto(translator.translate(translationKey, locale), fieldName));

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private boolean wasUserPasswordUpdated(String eTag, String username, PasswordChangeFormDto formData) {
        return userService.updateOwnPassword(
                username,
                formData.getNewPassword(),
                formData.getCurrentPassword(),
                sanitizeReceivedETag(eTag));
    }

    @DeleteMapping(value = "admin/users/{userId}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("If-Match") String eTag, @PathVariable Long userId) {
        userService.delete(userId, eTag);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "search/users/")
    public ResponseEntity<Page<UserBasicDto>> searchForUsers(@RequestBody UserSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(userService.searchForUsers(criteria, pageable).map(basicConverter));
    }
}
