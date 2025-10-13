package school.sorokin.event_manager.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Role;
import school.sorokin.event_manager.model.User;
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
    public User createForRegistration(User user) {
        checkLoginExist(user);
        user.setRole(Role.USER);
        UserEntity userEntity = userRepository.save(userMapper.toEntity(user));
        User result = userMapper.toBusinessEntity(userEntity);
        log.info("Create register success {}", result);
        return result;
    }

    @Transactional
    public User create(User user) {
        checkLoginExist(user);
        UserEntity userEntity = userRepository.save(userMapper.toEntity(user));
        User result = userMapper.toBusinessEntity(userEntity);
        log.info("Create user success {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " no found."));
        return userMapper.toBusinessEntity(userEntity);
    }

    private void checkLoginExist(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new EntityExistsException("User with login = " + user.getLogin() + " exist.");
        }
    }
}
