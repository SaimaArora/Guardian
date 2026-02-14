package guardianlink.repository;

import guardianlink.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//to save users, fetch users, find by id optional but useful
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
