package guardianlink.service;
import guardianlink.dto.HelpRequestResponse;
import guardianlink.exception.BadRequestException;
import guardianlink.exception.UnauthorizedException;
import guardianlink.model.Category;
import guardianlink.model.RequestStatus;
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
    public HelpRequestResponse createRequest(String name, Long categoryId, String email) {
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(()-> new RuntimeException("Category not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        HelpRequest request = new HelpRequest();
        request.setName(name);
        request.setStatus(RequestStatus.OPEN);//set default status before saving
        request.setCategory(category);
        request.setUser(user);
        HelpRequest saved = helpRequestRepository.save(request); //save will insert in dtbs
        return mapToResponse(saved);
    }

    // Get all help requests -> findAll() - select * from help_request
    public List<HelpRequestResponse> getAllRequests() {
        return helpRequestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    // Get requests for logged-in user
    public List<HelpRequestResponse> getRequestsForUser(String email) {
        return helpRequestRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get request by ID
    public HelpRequestResponse getRequestById(Long id) {
        HelpRequest request = helpRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Request not found"));

        return mapToResponse(request);
    }

    // Mark request as completed - find request, update status, return updated object
    public HelpRequestResponse completeRequest(Long id, String email) {

        HelpRequest request = helpRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Request not found"));

        if (request.getStatus() != RequestStatus.IN_PROGRESS) {
            throw new BadRequestException("Request is not in progress");
        }

        if (request.getAssignedVolunteer() == null ||
                !request.getAssignedVolunteer().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not assigned to this request");
        }

        request.setStatus(RequestStatus.COMPLETED);

        HelpRequest updated = helpRequestRepository.save(request);

        return mapToResponse(updated);
    }

    //delete request by id
    public void deleteRequest(Long id, String email) {

        HelpRequest request = helpRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Request not found"));

        if (!request.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not allowed to delete this request");
        }

        helpRequestRepository.delete(request);
    }

    //for controller claim request
    public HelpRequestResponse claimRequest(Long requestId, String volunteerEmail) {

        HelpRequest request = helpRequestRepository.findById(requestId)
                .orElseThrow(() -> new BadRequestException("Request not found"));

        if (request.getStatus() != RequestStatus.OPEN) {
            throw new BadRequestException("Request is not open");
        }

        User volunteer = userRepository.findByEmail(volunteerEmail)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!"VOLUNTEER".equals(volunteer.getRole().name())) {
            throw new UnauthorizedException("Only volunteers can claim requests");
        }
        List<HelpRequest> assigned =
                helpRequestRepository.findByAssignedVolunteerEmail(volunteerEmail);

        boolean hasActiveRequest = assigned.stream()
                .anyMatch(r -> r.getStatus().equals("IN_PROGRESS"));

        if(hasActiveRequest){
            throw new RuntimeException("Complete your current request before claiming another");
        }

        request.setStatus(RequestStatus.IN_PROGRESS);
        request.setAssignedVolunteer(volunteer);

        HelpRequest updated = helpRequestRepository.save(request);

        return mapToResponse(updated);
    }

    private HelpRequestResponse mapToResponse(HelpRequest request) {
        return new HelpRequestResponse(
                request.getId(),
                request.getName(),
                request.getStatus().name(),
                request.getCategory().getName(),
                request.getUser().getEmail(),
                request.getAssignedVolunteer() != null
                        ? request.getAssignedVolunteer().getEmail()
                        : null
        );
    }
    public List<HelpRequestResponse> getRequestsForVolunteer(String email) {
        return helpRequestRepository
                .findByAssignedVolunteerEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList(); //returns request where assignedemail == volunteer email
    }

    public List<HelpRequestResponse> getAssignedRequests(String email) {
        return helpRequestRepository.findByAssignedVolunteerEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
//jpa gave readymade methods like findbyid, findall, save

