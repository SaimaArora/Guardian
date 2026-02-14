package guardianlink.config;

import guardianlink.model.Category;
import guardianlink.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration //springboot will load the class
public class DataIntializer {

    @Bean //runs once when app starts (first run, inserts categories, next run does nothing)
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
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
        };
    }
}
