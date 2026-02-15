package guardianlink.controller;

import guardianlink.model.User;
import guardianlink.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") //frontend can call the backend
@RestController
@RequestMapping("/users") //get(/users) return all users
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
