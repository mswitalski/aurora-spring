package pl.lodz.p.aurora.users.web;

import org.modelmapper.ModelMapper;
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
import pl.lodz.p.aurora.users.domain.dto.AdminPasswordChangeFormDto;
import pl.lodz.p.aurora.users.domain.dto.PasswordChangeFormDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
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
    private final ModelMapper modelMapper;
    private final Translator translator;

    @Autowired
    public UserController(UserServiceImpl userService,
                          ModelMapper modelMapper,
                          Translator translator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.translator = translator;
    }

    @RequestMapping(value = "admin/users/", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createAsAdmin(@Validated @RequestBody UserDto userDto) {
        UserDto savedUser = convertToDto(userService.createAsAdmin(convertToEntity(userDto)));

        return ResponseEntity.ok().body(savedUser);
    }

    /**
     * Convert given User entity to UserDto object.
     *
     * @param user Object containing data about a user
     * @return UserDto object
     */
    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Convert given UserDto object to User entity.
     *
     * @param userDto DTO object containing data about a user
     * @return User entity
     */
    private User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @RequestMapping(value = "unitleader/users/", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createAsUnitLeader(@Validated @RequestBody UserDto userDto) {
        UserDto savedUser = convertToDto(userService.createAsUnitLeader(convertToEntity(userDto)));

        return ResponseEntity.ok().body(savedUser);
    }

    @RequestMapping(value = "users/", method = RequestMethod.GET)
    public ResponseEntity<Page<User>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAllByPage(pageable));
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findLoggedUser() {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return respondWithUserDto(userService.findByUsername(loggedUser.getUsername()));
    }

    @RequestMapping(value = "users/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        return respondWithUserDto(userService.findByUsername(username));
    }

    private ResponseEntity<UserDto> respondWithUserDto(User user) {
        return ResponseEntity.ok().eTag(Long.toString(user.getVersion())).body(convertToDto(user));
    }

    @RequestMapping(value = "admin/users/", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAsAdmin(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userService.updateAccount(sanitizeReceivedETag(eTag), convertToEntity(user));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "unitleader/users/", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAsUnitLeader(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userService.updateAccount(sanitizeReceivedETag(eTag), convertToEntity(user));

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#user.getUsername() == principal.username")
    @RequestMapping(value = "users/", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateOwnAccount(@RequestHeader("If-Match") String eTag, @Validated @RequestBody UserDto user) {
        userService.updateOwnAccount(sanitizeReceivedETag(eTag), convertToEntity(user));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "admin/users/{userId}/activation", method = RequestMethod.PUT)
    public ResponseEntity<Void> enableUser(@RequestHeader("If-Match") String eTag, @PathVariable Long userId) {
        userService.enable(userId, sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "admin/users/{userId}/deactivation", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> disableUser(@RequestHeader("If-Match") String eTag, @PathVariable Long userId) {
        userService.disable(userId, sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "admin/users/{userId}/role/{roleName}", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> assignRoleToUser(@RequestHeader("If-Match") String eTag, @PathVariable Long userId, @PathVariable String roleName) {
        userService.assignRole(userId, roleName, sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "admin/users/{userId}/role/{roleName}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDto> retractRoleFromUser(@RequestHeader("If-Match") String eTag, @PathVariable Long userId, @PathVariable String roleName) {
        userService.retractRole(userId, roleName, sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "admin/users/{userId}/password", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updatePasswordAsAdmin(
            @RequestHeader("If-Match") String eTag, @PathVariable Long userId, @Validated @RequestBody AdminPasswordChangeFormDto formData
    ) {
        userService.updatePasswordAsAdmin(userId, formData.getNewPassword(), sanitizeReceivedETag(eTag));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "users/password", method = RequestMethod.PUT)
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
}
