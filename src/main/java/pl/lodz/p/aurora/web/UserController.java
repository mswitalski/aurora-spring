package pl.lodz.p.aurora.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.domain.dto.UserDto;
import pl.lodz.p.aurora.domain.entity.User;
import pl.lodz.p.aurora.service.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing any requests related to user account.
 */
@RequestMapping("api/users/")
@RestController
public class UserController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDto> findAll() {
        return userService.findAll().stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        User foundUser = userService.findByUsername(username);

        if (foundUser == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(convertToDto(foundUser), HttpStatus.OK);
        }
    }

    /**
     * Convert given User object to UserDto.
     *
     * @param user Object containing data about a user
     * @return UserDto object
     */
    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
