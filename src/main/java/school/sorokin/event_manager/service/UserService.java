package school.sorokin.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sorokin.event_manager.model.Role;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.model.mapper.UserMapper;
import school.sorokin.event_manager.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public User createForRegistration(User user) {
        user.setRole(Role.USER);
        UserEntity userEntity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toBusinessEntity(userEntity);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " no found."));
        return userMapper.toBusinessEntity(userEntity);
    }

    @Transactional(readOnly = true)
    public User getByLogin(String login) {
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() ->
                new EntityNotFoundException("User with login = " + login + " no found."));
        return userMapper.toBusinessEntity(userEntity);
    }

}
