package guardianlink.service;

import guardianlink.model.HelpRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

//creates requests, updates status, manages storage

@Service
public class HelpRequestService {

    private final Map<Long, HelpRequest> helpRequestStore = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Create a new help request - set the id and status and put in map
    public HelpRequest createRequest(HelpRequest request) {
        Long id = idGenerator.getAndIncrement();
        request.setId(id);
        request.setStatus("OPEN");

        helpRequestStore.put(id, request);
        return request;
    }

    // Get all help requests
    public List<HelpRequest> getAllRequests() {
        return new ArrayList<>(helpRequestStore.values());
    }

    // Get request by ID
    public HelpRequest getRequestById(Long id) {
        return helpRequestStore.get(id);
    }

    // Mark request as completed - find request, update status, return updated object
    public HelpRequest completeRequest(Long id) {
        HelpRequest request = helpRequestStore.get(id);
        if (request != null) {
            request.setStatus("COMPLETED");
        }
        return request;
    }
}
