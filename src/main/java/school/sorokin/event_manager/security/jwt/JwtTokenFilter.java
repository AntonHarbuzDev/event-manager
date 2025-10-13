package school.sorokin.event_manager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import school.sorokin.event_manager.security.CustomUserDetailsService;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenManager jwtTokenManager;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenManager = jwtTokenManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwtToken = getTokenFromRequest(request);
            if (jwtToken != null) {
                String login = jwtTokenManager.getLoginFromToken(jwtToken);
                UserDetails user = customUserDetailsService.loadUserByUsername(login);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHerder = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHerder) && authHerder.startsWith("Bearer ")) {
            return authHerder.substring(7);
        }
        return null;
    }
}
