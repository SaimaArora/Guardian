package guardianlink.controller;

import guardianlink.model.HelpRequest;
import guardianlink.service.HelpRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//handles http request - connects http -> java and translates urls, http methods and json bodies

@RestController
@RequestMapping("/requests") // every api starts with request, returns json automatically
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    // POST: create new help request (JSON)
    @PostMapping //@requestBody - json input, spring reads json and converts to helprequest and passes to service
    public HelpRequest createRequest(@RequestBody HelpRequest request) {
        return helpRequestService.createRequest(request);
    }

    // GET: all requests
    @GetMapping
    public List<HelpRequest> getAllRequests() {
        return helpRequestService.getAllRequests();
    }

    // GET: request by id
    @GetMapping("/{id}")
    public HelpRequest getRequestById(@PathVariable Long id) {
        return helpRequestService.getRequestById(id);
    }

    // PUT: mark request as completed
    @PutMapping("/{id}/complete")
    public HelpRequest completeRequest(@PathVariable Long id) {
        return helpRequestService.completeRequest(id);
    }
}
