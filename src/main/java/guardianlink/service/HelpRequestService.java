package guardianlink.service;
import guardianlink.model.Category;
import guardianlink.repository.CategoryRepository;
import guardianlink.model.HelpRequest;
import guardianlink.repository.HelpRequestRepository;
import guardianlink.model.User;
import guardianlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

//creates requests, updates status, manages storage

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final CategoryRepository categoryRepository; //helprequest must belong to a valid user and category
    private final UserRepository userRepository;
    //spring gives repo automatically through constructor injection
    public HelpRequestService(HelpRequestRepository helpRequestRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.helpRequestRepository = helpRequestRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Create a new help request
    //takes catid from client, loads cate from db, not found then error, creates help request links it to category and saves
    public HelpRequest createRequest(String name, Long categoryId, String email) {
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(()-> new RuntimeException("Category not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        HelpRequest request = new HelpRequest();
        request.setName(name);
        request.setStatus("OPEN"); //set default status before saving
        request.setCategory(category);
        request.setUser(user);
        return helpRequestRepository.save(request); //save will insert in dtbs
    }

    // Get all help requests -> findAll() - select * from help_request
    public List<HelpRequest> getRequestsForUser(String email) {
        return helpRequestRepository.findByUserEmail(email);
    }

    // Get request by ID
    public HelpRequest getRequestById(Long id) {
        Optional<HelpRequest> optional = helpRequestRepository.findById(id);
        return optional.orElse(null);
    }

    // Mark request as completed - find request, update status, return updated object
    public HelpRequest completeRequest(Long id, String email) {
        HelpRequest request = helpRequestRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Request not found"));
//        if(!request.getUser().getEmail().equals(email)) {
//            throw new RuntimeException("You are allowed to complete this request");
//        }
        if(!"OPEN".equals(request.getStatus())) {
            throw new RuntimeException("Request already completed");
        }
        request.setStatus("COMPLETED");
        return helpRequestRepository.save(request);
    }

    //delete request by id
    public void deleteRequest(Long id, String email) {
        HelpRequest request = helpRequestRepository.findById(id)
                        .orElseThrow(()-> new RuntimeException("Request not found"));
        if(!request.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this request");
        }
        helpRequestRepository.delete(request); //runs delete from help_request where id=?
    }
    public List<HelpRequest> getAllRequests() {
        return helpRequestRepository.findAll();
    }
}
//jpa gave readymade methods like findbyid, findall, save

