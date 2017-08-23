package pl.lodz.p.aurora.users.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.common.util.EntityVersionTransformer;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing any requests related to users account.
 */
@RequestMapping("api/")
@RestController
public class UserController extends BaseController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserServiceImpl userService, ModelMapper modelMapper, EntityVersionTransformer transformer) {
        super(transformer);
        this.userService = userService;
        this.modelMapper = modelMapper;
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
    public List<UserDto> findAll() {
        return userService.findAll().stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "users/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        User foundUser = userService.findByUsername(username);

        return new ResponseEntity<>(convertToDto(foundUser), prepareETagHeaders(foundUser), HttpStatus.OK);
    }

    @RequestMapping(value = "admin/users/", method = RequestMethod.PUT)
    public UserDto updateAsAdmin(@RequestHeader("ETag") String eTag, @RequestBody UserDto user) {
        return convertToDto(userService.update(sanitizeReceivedETag(eTag), convertToEntity(user)));
    }

    @RequestMapping(value = "unitleader/users/", method = RequestMethod.PUT)
    public UserDto updateAsUnitLeader(@RequestHeader("ETag") String eTag, @RequestBody UserDto user) {
        return convertToDto(userService.update(sanitizeReceivedETag(eTag), convertToEntity(user)));
    }

    // Here will be something like
    // @PreAuthorize("isFullyAuthenticated() and #user.getUsername() == principal.name")
    @RequestMapping(value = "users/", method = RequestMethod.PUT)
    public UserDto updateOwnAccount(@RequestHeader("ETag") String eTag, @RequestBody UserDto user) {
        return convertToDto(userService.update(sanitizeReceivedETag(eTag), convertToEntity(user)));
    }

    @RequestMapping(value = "admin/users/{userId}/activation", method = RequestMethod.PUT)
    public UserDto enableUser(@RequestHeader("ETag") String eTag, @PathVariable Long userId) {
        return convertToDto(userService.enable(userId, eTag));
    }

    @RequestMapping(value = "admin/users/{userId}/deactivation", method = RequestMethod.PUT)
    public UserDto disableUser(@RequestHeader("ETag") String eTag, @PathVariable Long userId) {
        return convertToDto(userService.disable(userId, eTag));
    }

    @RequestMapping(value = "users/{userId}/role/{roleName}", method = RequestMethod.PUT)
    public UserDto assignRoleToUser(@RequestHeader("ETag") String eTag, @PathVariable Long userId, @PathVariable String roleName) {
        return convertToDto(userService.assignRole(userId, roleName, eTag));
    }
}
