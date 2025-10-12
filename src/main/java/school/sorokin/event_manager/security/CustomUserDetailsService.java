package school.sorokin.event_manager.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.repository.UserRepository;
import school.sorokin.event_manager.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException("User with login = " + username + " no found"));
        return User.withUsername(username)
                .password(userEntity.getPassword())
                .authorities(userEntity.getRole().name())
                .build();
    }
}
