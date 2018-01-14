package pl.lodz.p.aurora.mus.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.msh.web.dto.ValidationMessageDto;
import pl.lodz.p.aurora.msh.util.Translator;
import pl.lodz.p.aurora.msh.web.controller.BaseController;
import pl.lodz.p.aurora.mus.web.converter.UserDtoToEntityConverter;
import pl.lodz.p.aurora.mus.web.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.mus.web.dto.PasswordChangeFormDto;
import pl.lodz.p.aurora.mus.web.dto.UserDto;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.service.common.ProfileService;
import pl.lodz.p.aurora.mus.service.common.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequestMapping(value = "api/v1/profile", headers = "Requester-Role=ANY")
@RestController
public class UserProfileController extends BaseController {

    private final ProfileService profileService;
    private final UserService userService;
    private final Translator translator;
    private final UserEntityToDtoConverter entityToDtoConverter = new UserEntityToDtoConverter();
    private final UserDtoToEntityConverter dtoToEntityConverter = new UserDtoToEntityConverter();

    @Autowired
    public UserProfileController(ProfileService profileService, UserService userService, Translator translator) {
        this.profileService = profileService;
        this.userService = userService;
        this.translator = translator;
    }

    @GetMapping
    public ResponseEntity<UserDto> findLoggedUser(@AuthenticationPrincipal User activeUser) {

        return respondWithETag(userService.findById(activeUser.getId()), entityToDtoConverter);
    }

    @PreAuthorize("#user.username == principal.username")
    @PutMapping
    public ResponseEntity<Void> update(@Validated @RequestBody UserDto user, @RequestHeader("If-Match") String eTag) {
        profileService.update(sanitizeReceivedETag(eTag), dtoToEntityConverter.convert(user));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "password")
    public ResponseEntity<List<ValidationMessageDto>> updatePassword(@Validated @RequestBody PasswordChangeFormDto formData, @RequestHeader("If-Match") String eTag, @AuthenticationPrincipal User activeUser) {
        Locale locale = LocaleContextHolder.getLocale();

        if (!formData.getNewPassword().equals(formData.getNewPasswordRepeated())) {
            return preparePasswordChangeErrorResponse("PasswordChangeFormDto.newPasswordRepeated.Invalid", "new-password-repeated", locale);
        }
        if (wasUserPasswordUpdated(eTag, activeUser.getUsername(), formData)) {
            return ResponseEntity.noContent().build();

        } else {
            return preparePasswordChangeErrorResponse("PasswordChangeFormDto.currentPassword.Invalid", "current-password", locale);
        }
    }

    private ResponseEntity<List<ValidationMessageDto>> preparePasswordChangeErrorResponse(String translationKey, String fieldName, Locale locale) {
        List<ValidationMessageDto> errorMessage =
                Collections.singletonList(new ValidationMessageDto(translator.translate(translationKey, locale), fieldName));

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private boolean wasUserPasswordUpdated(String eTag, String username, PasswordChangeFormDto formData) {
        return profileService.updatePassword(
                username,
                formData.getNewPassword(),
                formData.getCurrentPassword(),
                sanitizeReceivedETag(eTag)
        );
    }
}
