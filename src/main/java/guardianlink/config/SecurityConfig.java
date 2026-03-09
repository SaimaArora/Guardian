package guardianlink.config;


import guardianlink.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration //this class has configuration for the application - spring loads it automatically when app starts
public class SecurityConfig {
    //injecting JWT filter in class
    private final JwtAuthFilter jwtAuthFilter; //dependency injection

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean //should always be hashed in dtbs
    public PasswordEncoder passwordEncoder() { //used to hash passwords on register and verify passwords on login
        return new BCryptPasswordEncoder();
    }


    // ✅ Security rules + CORS + CSRF
    @Bean //gives security rules, auth logic, authorization logic
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())   // enable CORS
                .csrf(csrf -> csrf.disable())      // disable CSRF for APIs, as we use jwt - stateless
                .authorizeHttpRequests(auth -> auth //defines who can access what
                        //public endpoints - anyone can access - as users have to login before authentication
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()

                        //authenticated endpoints
                        .requestMatchers(HttpMethod.POST, "/requests/**").hasRole("USER") /// user can create requests
                        .requestMatchers(HttpMethod.PUT, "/requests/**").hasRole("VOLUNTEER") //volunteers can claim/complete requests
                        .requestMatchers(HttpMethod.GET, "/requests/**").authenticated() //both roles can access - but you must be logged in
                        .anyRequest().authenticated() //others user must be authenticated
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //insert my filter before spring filter

        return http.build();
    }

    // ✅ Global CORS configuration for React
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
