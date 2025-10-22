package school.sorokin.event_manager.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Role;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.SingUpRequest;
import school.sorokin.event_manager.model.dto.UserDto;
import school.sorokin.event_manager.model.dto.UserShowDto;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.model.mapper.UserMapper;
import school.sorokin.event_manager.repository.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserShowDto createForRegistration(SingUpRequest user) {
        User userCreated = userMapper.toBusinessEntity(user);
        checkLoginExist(userCreated);
        userCreated.setRole(Role.USER);
        UserEntity userEntity = userRepository.save(userMapper.toEntity(userCreated));
        UserShowDto result = userMapper.toShowDto(userEntity);
        log.info("Create register success with login - {}", result.getLogin());
        return result;
    }

    @Transactional
    public void create(UserDto user) {
        User userCreated = userMapper.toBusinessEntity(user);
        checkLoginExist(userCreated);
        UserEntity userEntity = userRepository.save(userMapper.toEntity(userCreated));
        log.info("Create user success  with login - {}", userEntity.getLogin());
    }

    @Transactional(readOnly = true)
    public UserShowDto getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " no found."));
        return userMapper.toShowDto(userEntity);
    }

    private void checkLoginExist(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new EntityExistsException("User with login = " + user.getLogin() + " exist.");
        }
    }
}
