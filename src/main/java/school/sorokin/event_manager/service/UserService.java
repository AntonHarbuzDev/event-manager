package school.sorokin.event_manager.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Role;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.SingUpRequest;
import school.sorokin.event_manager.model.dto.UserDto;
import school.sorokin.event_manager.model.dto.UserShowDto;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.repository.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserShowDto createForRegistration(SingUpRequest user) {
        User userCreated = toBusinessEntity(user);
        checkLoginExist(userCreated);
        userCreated.setRole(Role.USER);
        UserEntity userEntity = userRepository.save(toEntity(userCreated));
        UserShowDto result = toShowDto(userEntity);
        log.info("Create register success with login - {}", result.getLogin());
        return result;
    }

    @Transactional
    public void createUser(UserDto user) {
        User userCreated = toBusinessEntity(user);
        checkLoginExist(userCreated);
        UserEntity userEntity = userRepository.save(toEntity(userCreated));
        log.info("Create user success  with login - {}", userEntity.getLogin());
    }

    @Transactional(readOnly = true)
    public UserShowDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " no found."));
        return toShowDto(userEntity);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserEntityByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() ->
                new EntityNotFoundException("User with login = " + login + " no found."));
    }

    private void checkLoginExist(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new EntityExistsException("User with login = " + user.getLogin() + " exist.");
        }
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getAge(),
                user.getRole()
        );
    }

    private User toBusinessEntity(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getLogin(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getAge(),
                dto.getRole()
        );
    }

    private User toBusinessEntity(SingUpRequest singUpRequest) {
        return new User(
                null,
                singUpRequest.getLogin(),
                passwordEncoder.encode(singUpRequest.getPassword()),
                singUpRequest.getAge(),
                Role.USER
        );
    }

    private UserShowDto toShowDto(UserEntity userEntity) {
        return new UserShowDto(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                userEntity.getRole()
        );
    }

}
