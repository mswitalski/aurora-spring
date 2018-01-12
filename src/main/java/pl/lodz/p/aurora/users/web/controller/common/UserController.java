package pl.lodz.p.aurora.users.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.controller.BaseController;
import pl.lodz.p.aurora.users.web.converter.UserBasicDtoConverter;
import pl.lodz.p.aurora.users.web.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.users.web.dto.UserBasicDto;
import pl.lodz.p.aurora.users.web.dto.UserDto;
import pl.lodz.p.aurora.users.web.dto.UserSearchDto;
import pl.lodz.p.aurora.users.service.common.UserService;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=ANY")
@RestController
public class UserController extends BaseController {

    private final UserService userService;
    private final UserEntityToDtoConverter entityToDtoConverter = new UserEntityToDtoConverter();
    private final UserBasicDtoConverter basicConverter = new UserBasicDtoConverter();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<UserBasicDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAllByPage(pageable).map(basicConverter));
    }

    @GetMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<UserDto> findById(@PathVariable Long userId) {
        return respondWithETag(userService.findById(userId), entityToDtoConverter);
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<UserBasicDto>> searchForUsers(@RequestBody UserSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(userService.search(criteria, pageable).map(basicConverter));
    }
}
