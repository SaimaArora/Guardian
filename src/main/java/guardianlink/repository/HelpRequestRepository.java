package guardianlink.repository;

import guardianlink.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findByUserEmail(String email); //select * from help_request join users on where users.email
}
//spring automatically creates implementation at runtime, injects it where needed, handles sql