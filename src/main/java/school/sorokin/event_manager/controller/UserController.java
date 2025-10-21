package school.sorokin.event_manager.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sorokin.event_manager.model.JwtTokenResponse;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.SignUpRequest;
import school.sorokin.event_manager.model.dto.SingInRequest;
import school.sorokin.event_manager.model.dto.UserDto;
import school.sorokin.event_manager.model.dto.UserShowDto;
import school.sorokin.event_manager.model.mapper.UserMapper;
import school.sorokin.event_manager.security.jwt.JwtAuthenticationService;
import school.sorokin.event_manager.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtAuthenticationService jwtAuthenticationService;

    public UserController(UserService userService, UserMapper userMapper, JwtAuthenticationService jwtAuthenticationService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createRegistration(@RequestBody @Valid SignUpRequest signUpRequest) {
        log.info("Get request for sign-un: login={}", signUpRequest.getLogin());
        User user = userService.createForRegistration(userMapper.toBusinessEntity(signUpRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserShowDto> getUserById(@PathVariable Long id) {
        log.info("Get request for get user by id: id={}", id);
        User user = userService.getById(id);
        return ResponseEntity.ok().body(userMapper.toShowDto(user));
    }

    @PostMapping("/auth")
    @PreAuthorize("permitAll()")
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody @Valid SingInRequest singInRequest) {
        log.info("Get request for authenticate user: login={}", singInRequest.getLogin());
        String token = jwtAuthenticationService.authenticateUser(singInRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtTokenResponse(token));
    }
}
