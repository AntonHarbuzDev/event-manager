package school.sorokin.event_manager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.SignUpRequest;
import school.sorokin.event_manager.model.dto.UserDto;
import school.sorokin.event_manager.model.dto.UserShowDto;
import school.sorokin.event_manager.model.mapper.UserMapper;
import school.sorokin.event_manager.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> createRegistration(@RequestBody @Valid SignUpRequest signUpRequest) {
        User user = userService.createForRegistration(userMapper.toBusinessEntity(signUpRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserShowDto> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok().body(userMapper.toShowDto(user));
    }
}
