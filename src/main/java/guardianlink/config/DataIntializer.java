package guardianlink.config;

import guardianlink.model.User;
import guardianlink.repository.UserRepository;
import guardianlink.model.Category;
import guardianlink.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration //springboot will load the class
public class DataIntializer {

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
                        new User("Alice", "alice@example.com"),
                        new User("Bob", "bob@example.com"),
                        new User("Charlie", "charlie@example.com")
                );
                userRepository.saveAll(defaultUsers);
                System.out.println("Default users inserted.");
            }
        };
    }
}
