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
import pl.lodz.p.aurora.common.util.EntityVersionTransformer;
import pl.lodz.p.aurora.common.util.Translator;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.UserEntityToDtoConverter;
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
    private final UserEntityToDtoConverter entityToDtoConverter;

    @Autowired
    public UserController(UserServiceImpl userService,
                          ModelMapper modelMapper,
                          EntityVersionTransformer transformer,
                          Translator translator,
                          UserEntityToDtoConverter entityToDtoConverter) {
        super(transformer);
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.translator = translator;
        this.entityToDtoConverter = entityToDtoConverter;
    }

    @RequestMapping(value = "admin/users/", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createAsAdmin(@Validated @RequestBody UserDto userDto) {
        User createdUser = userService.createAsAdmin(convertToEntity(userDto));

        return new ResponseEntity<>(convertToDto(createdUser), prepareETagHeaders(createdUser), HttpStatus.OK);
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
        User createdUser = userService.createAsUnitLeader(convertToEntity(userDto));

        return new ResponseEntity<>(convertToDto(createdUser), prepareETagHeaders(createdUser), HttpStatus.OK);
    }

    @RequestMapping(value = "users/", method = RequestMethod.GET)
    public Page<User> findAll(Pageable pageable) {
        return userService.findAllByPage(pageable);
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
        return new ResponseEntity<>(convertToDto(user), prepareETagHeaders(user), HttpStatus.OK);
    }

    @RequestMapping(value = "admin/users/", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateAsAdmin(@RequestHeader("ETag") String eTag, @Validated @RequestBody UserDto user) {
        return respondWithUserDto(userService.updateAccount(sanitizeReceivedETag(eTag), convertToEntity(user)));
    }

    @RequestMapping(value = "unitleader/users/", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateAsUnitLeader(@RequestHeader("ETag") String eTag, @Validated @RequestBody UserDto user) {
        return respondWithUserDto(userService.updateAccount(sanitizeReceivedETag(eTag), convertToEntity(user)));
    }

    @PreAuthorize("#user.getUsername() == principal.username")
    @RequestMapping(value = "users/", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateOwnAccount(@RequestHeader("ETag") String eTag, @Validated @RequestBody UserDto user) {
        return respondWithUserDto(userService.updateOwnAccount(sanitizeReceivedETag(eTag), convertToEntity(user)));
    }

    @RequestMapping(value = "admin/users/{userId}/activation", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> enableUser(@RequestHeader("ETag") String eTag, @PathVariable Long userId) {
        return respondWithUserDto(userService.enable(userId, sanitizeReceivedETag(eTag)));
    }

    @RequestMapping(value = "admin/users/{userId}/deactivation", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> disableUser(@RequestHeader("ETag") String eTag, @PathVariable Long userId) {
        return respondWithUserDto(userService.disable(userId, sanitizeReceivedETag(eTag)));
    }

    @RequestMapping(value = "admin/users/{userId}/role/{roleName}", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> assignRoleToUser(@RequestHeader("ETag") String eTag, @PathVariable Long userId, @PathVariable String roleName) {
        return respondWithUserDto(userService.assignRole(userId, roleName, sanitizeReceivedETag(eTag)));
    }

    @RequestMapping(value = "admin/users/{username}/password", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updatePasswordAsAdmin(
            @RequestHeader("ETag") String eTag, @PathVariable String username, @Validated @RequestBody AdminPasswordChangeFormDto formData
    ) {
        return respondWithUserDto(userService.updatePasswordAsAdmin(
                username,
                formData.getNewPassword(),
                sanitizeReceivedETag(eTag)));
    }

    @RequestMapping(value = "users/password", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateOwnPassword(
            @RequestHeader("ETag") String eTag, @Validated @RequestBody PasswordChangeFormDto formData
    ) {
        Locale locale = LocaleContextHolder.getLocale();

        if (!formData.getNewPassword().equals(formData.getNewPasswordRepeated())) {
            return preparePasswordChangeErrorResponse("PasswordChangeFormDto.newPasswordRepeated.Invalid", "new-password-repeated", locale);
        }

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (wasUserPasswordUpdated(eTag, loggedUser.getUsername(), formData)) {
            User updatedUser = userService.findByUsername(loggedUser.getUsername());

            return new ResponseEntity<>(convertToDto(updatedUser), prepareETagHeaders(updatedUser), HttpStatus.OK);

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
