package guardianlink.service;

import guardianlink.dto.LoginRequest;
import guardianlink.dto.RegisterRequest;
import guardianlink.exception.UnauthorizedException;
import guardianlink.exception.BadRequestException;

import guardianlink.model.Role;
import guardianlink.model.User;
import guardianlink.repository.UserRepository;
import guardianlink.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }
        System.out.println("Register role: " + request.getRole());
        Role role = request.getRole() != null ? request.getRole() : Role.USER;
        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role
        );
        userRepository.save(user);
    }

    public Map<String, String> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new UnauthorizedException("Invalid email"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }
        String token = jwtUtil.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().name());
        return response;
    }
}
