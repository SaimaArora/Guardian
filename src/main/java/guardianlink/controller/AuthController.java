package guardianlink.controller;

import guardianlink.dto.LoginRequest;
import guardianlink.dto.RegisterRequest;
import guardianlink.model.User;
import guardianlink.repository.UserRepository;
import guardianlink.security.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000") //frontend can call
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //register
    @PostMapping("/register") // -> /auth/register - checks if email exists, and creates user
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        //check if email exists
        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        User user = new User(
                request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()) //password hased before saving
        );
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
    //Login
    @PostMapping("/login") // -> /auth/login - finds email by user, checks password
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email");
        }
        User user = userOpt.get();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) { //compare raw password vs hashed password
            return ResponseEntity.status(401).body("Invalid password");
        }
        String token = JwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
}
