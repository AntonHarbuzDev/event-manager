package school.sorokin.event_manager;

import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.Role;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.UserDto;
import school.sorokin.event_manager.model.mapper.UserMapper;
import school.sorokin.event_manager.service.UserService;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;
    private final UserMapper userMapper;

    public DataInitializer(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeDefaultUser() {
        UserDto userDto = new UserDto(null, "user", "user", 20, Role.USER);
        User user = userMapper.toBusinessEntity(userDto);
        try {
            userService.create(user);
        } catch (EntityExistsException e) {
            log.info("User with login user already exist.");
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeDefaultAdmin() {
        UserDto userDto = new UserDto(null, "admin", "admin", 20, Role.ADMIN);
        User user = userMapper.toBusinessEntity(userDto);
        try {
            userService.create(user);
        } catch (EntityExistsException e) {
            log.info("User with login admin already exist.");
        }
    }
}
