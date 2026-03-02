package guardianlink.config;

import guardianlink.model.Role;
import guardianlink.model.User;
import guardianlink.repository.UserRepository;
import guardianlink.model.Category;
import guardianlink.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration //springboot will load the class
public class DataIntializer {

    private final PasswordEncoder passwordEncoder;

    public DataIntializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean //runs once when app starts (first run, inserts categories and users, next run does nothing)
    CommandLineRunner initData(CategoryRepository categoryRepository, UserRepository userRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                List<Category> defaultCategories = List.of(
                        new Category("Medical"),
                        new Category("Food"),
                        new Category("Emergency"),
                        new Category("Transport")
                );
                categoryRepository.saveAll(defaultCategories);
                System.out.println("Default categories inserted");
            }
            if (userRepository.count() == 0) {
                List<User> defaultUsers = List.of(
                        new User("Alice", "alice@example.com", passwordEncoder.encode("123456"),
                                Role.USER),
                        new User("Bob", "bob@example.com", passwordEncoder.encode("123456"),
                                Role.USER),
                        new User("Charlie", "charlie@example.com", passwordEncoder.encode("123456"),
                                Role.USER)
                );
                userRepository.saveAll(defaultUsers);
                System.out.println("Default users inserted.");
            }
        };
    }
}
