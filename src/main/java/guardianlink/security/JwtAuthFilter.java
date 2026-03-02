package guardianlink.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * SImple filter that extracts Bearer token, validates it via jwtutil
 * sets usernamepasswordauth  with role as granted authority
 * just leaves the request unaithenticated so spring will reject endpoints requiring auth**/

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7).trim(); // IMPORTANT: strip "Bearer "
                // Validate token and extract claims
                String email = jwtUtil.extractEmail(token); // throws if invalid
                String role = jwtUtil.extractRole(token); // now static (see change below)

                // Map role claim to a SimpleGrantedAuthority
                var authority = new SimpleGrantedAuthority("ROLE_"+role);
                var auth = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JWTVerificationException | IllegalArgumentException ex) {
            // token invalid or malformed — we *do not* set authentication
            // Let Spring handle rejecting requests that require authentication.
            logger.debug("JWT validation failed: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
