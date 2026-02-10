package guardianlink.repository;

import guardianlink.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
}
//spring automatically creates implementation at runtime, injects it where needed, handles sql