package pl.lodz.p.aurora.users.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.web.BaseController;
import pl.lodz.p.aurora.users.domain.converter.UserBasicDtoConverter;
import pl.lodz.p.aurora.users.domain.converter.UserEntityToDtoConverter;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;
import pl.lodz.p.aurora.users.domain.dto.UserDto;
import pl.lodz.p.aurora.users.domain.dto.UserSearchDto;
import pl.lodz.p.aurora.users.service.common.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/users/", headers = "Requester-Role=ALL")
@RestController
public class UserController extends BaseController {

    private final UserService userService;
    private final UserEntityToDtoConverter entityToDtoConverter;
    private final UserBasicDtoConverter basicConverter;

    @Autowired
    public UserController(UserService userService, UserEntityToDtoConverter entityToDtoConverter, UserBasicDtoConverter basicConverter) {
        this.userService = userService;
        this.entityToDtoConverter = entityToDtoConverter;
        this.basicConverter = basicConverter;
    }

    @GetMapping
    public ResponseEntity<List<UserBasicDto>> findAll() {
        return ResponseEntity
                .ok()
                .body(userService.findAll().stream().map(basicConverter::convert).collect(Collectors.toList()));
    }

    @GetMapping(value = "paged/")
    public ResponseEntity<Page<UserBasicDto>> findAllByPage(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAllByPage(pageable).map(basicConverter));
    }

    @GetMapping(value = "{userId:[\\d]+}")
    public ResponseEntity<UserDto> findById(@PathVariable Long userId) {
        return respondWithConversion(userService.findById(userId), entityToDtoConverter);
    }

    @PostMapping(value = "search/")
    public ResponseEntity<Page<UserBasicDto>> searchForUsers(@RequestBody UserSearchDto criteria, Pageable pageable) {
        return ResponseEntity.ok().body(userService.search(criteria, pageable).map(basicConverter));
    }
}
