package school.sorokin.event_manager.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import school.sorokin.event_manager.model.dto.SingInRequest;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    public JwtAuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(SingInRequest singInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                singInRequest.getLogin(), singInRequest.getPassword()));
        return jwtTokenManager.generateToken(singInRequest.getLogin());
    }
}
