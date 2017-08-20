package pl.lodz.p.aurora.users.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
public class UserController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "admin/users/", method = RequestMethod.POST)
    public UserDto createAsAdmin(@Validated @RequestBody UserDto userDto) {
        return convertToDto(userService.createAsAdmin(convertToEntity(userDto)));
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

    @RequestMapping(value = "users/", method = RequestMethod.POST)
    public UserDto createAsUnitLeader(@Validated @RequestBody UserDto userDto) {
        return convertToDto(userService.createAsUnitLeader(convertToEntity(userDto)));
    }

    @RequestMapping(value = "users/", method = RequestMethod.GET)
    public List<UserDto> findAll() {
        return userService.findAll().stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "users/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        User foundUser = userService.findByUsername(username);

        if (foundUser == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(convertToDto(foundUser), HttpStatus.OK);
        }
    }
}
