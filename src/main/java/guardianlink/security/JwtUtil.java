package guardianlink.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import guardianlink.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final Algorithm algorithm;
    private final long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationTime) { // 24 hrs
        this.algorithm = Algorithm.HMAC256(secret); //how token is signed(signature algo) - has based mess
        this.expirationTime = expirationTime; //how long it is valid
    }

    //runs during login
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }
    public String extractEmail(String token) {
        return JWT.require(algorithm)
                .build().verify(token).getSubject();
    }
    public String extractRole(String token) {
        return JWT.require(algorithm)
                .build().verify(token).getClaim("role").asString();
    }
}
