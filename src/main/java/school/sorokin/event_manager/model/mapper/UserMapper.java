package school.sorokin.event_manager.model.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.Role;
import school.sorokin.event_manager.model.User;
import school.sorokin.event_manager.model.dto.SignUpRequest;
import school.sorokin.event_manager.model.dto.UserDto;
import school.sorokin.event_manager.model.dto.UserShowDto;
import school.sorokin.event_manager.model.entity.UserEntity;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getAge(),
                user.getRole()
        );
    }

    public User toBusinessEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getAge(),
                userEntity.getRole()
        );
    }

    public User toBusinessEntity(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getLogin(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getAge(),
                dto.getRole()
        );
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getAge(),
                user.getRole()
        );
    }

    public User toBusinessEntity(SignUpRequest signUpRequest) {
        return new User(
                null,
                signUpRequest.getLogin(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAge(),
                Role.USER
        );
    }

    public UserShowDto toShowDto(User user) {
        return new UserShowDto(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                user.getRole()
        );
    }
}
