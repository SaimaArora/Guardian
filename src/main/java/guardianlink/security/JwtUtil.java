package guardianlink.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import guardianlink.model.User;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "guardianlink_super_secret_key";
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24h

    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public static String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }
    public static String getRole(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }

    public static String validateAndGetEmail(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }
    public static String getRoleFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }
}
