package guardianlink.service;

import guardianlink.model.HelpRequest;
import guardianlink.repository.HelpRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

//creates requests, updates status, manages storage

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;

    //spring gives repo automatically through constructor injection
    public HelpRequestService(HelpRequestRepository helpRequestRepository) {
        this.helpRequestRepository = helpRequestRepository;
    }

    // Create a new help request
    public HelpRequest createRequest(HelpRequest request) {
        request.setStatus("OPEN"); //set default status before saving

        return helpRequestRepository.save(request); //save will insert in dtbs
    }

    // Get all help requests -> findAll() - select * from help_request
    public List<HelpRequest> getAllRequests() {
        return helpRequestRepository.findAll();
    }

    // Get request by ID
    public HelpRequest getRequestById(Long id) {
        Optional<HelpRequest> optional = helpRequestRepository.findById(id);
        return optional.orElse(null);
    }

    // Mark request as completed - find request, update status, return updated object
    public HelpRequest completeRequest(Long id) {
        Optional<HelpRequest> optional = helpRequestRepository.findById(id);
        if(optional.isPresent()) {
            HelpRequest request = optional.get();
            request.setStatus("Completed");
            return helpRequestRepository.save(request);
        }
        return null;
    }

    //delete request by id
    public void deleteRequest(Long id) {
        helpRequestRepository.deleteById(id); //runs delete from help_request where id=?
    }
}
//jpa gave readymade methods like findbyid, findall, save

