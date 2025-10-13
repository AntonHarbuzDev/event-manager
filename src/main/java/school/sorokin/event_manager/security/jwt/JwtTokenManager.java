package school.sorokin.event_manager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenManager {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(String login) {
        return Jwts.builder()
                .subject(login)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .compact();
    }

    public String getLoginFromToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }
}
