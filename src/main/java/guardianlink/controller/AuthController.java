package guardianlink.controller;

import guardianlink.dto.LoginRequest;
import guardianlink.dto.RegisterRequest;
import guardianlink.service.AuthService;

import guardianlink.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000") //frontend can call
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    //register
    @PostMapping("/register") // -> /auth/register - checks if email exists, and creates user
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
    }
    //Login
    @PostMapping("/login") // -> /auth/login - finds email by user, checks password
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login Successful", authService.login(request)));
    }
}
