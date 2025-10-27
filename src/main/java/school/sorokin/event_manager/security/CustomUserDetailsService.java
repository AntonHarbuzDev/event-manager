package school.sorokin.event_manager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import school.sorokin.event_manager.model.entity.UserEntity;
import school.sorokin.event_manager.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Load user by username = {}", username);
        if (username == null || username.isEmpty()) {
            throw new BadCredentialsException("Username cannot be empty");
        }
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException("User with login = " + username + " no found"));
        return User.withUsername(username)
                .password(userEntity.getPassword())
                .authorities(userEntity.getRole().name())
                .build();
    }
}
