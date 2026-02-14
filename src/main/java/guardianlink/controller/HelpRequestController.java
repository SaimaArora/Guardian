package guardianlink.controller;

import guardianlink.dto.CreateHelpRequestDto;
import guardianlink.model.HelpRequest;
import guardianlink.service.HelpRequestService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin; //for CORS

//handles http request - connects http -> java and translates urls, http methods and json bodies

@CrossOrigin(origins = "http://localhost:3000") //enable cross origin resource sharing(when allowed in springboot, react can call the apis, otherwise browser will block react to spring calls
@RestController
@RequestMapping("/requests") // every api starts with request, returns json automatically
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    // POST: create new help request (JSON)
    //@valid tells spring to validate request body, if fails - return 400 bad request, controller method not executed, to protect db
    @PostMapping //@requestBody - json input, spring reads json and converts to helprequest and passes to service
    public HelpRequest createRequest(@Valid @RequestBody CreateHelpRequestDto dto) {
        return helpRequestService.createRequest(dto.getName(), dto.getCategoryId());
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

    @DeleteMapping("/{id}") //maps http delete requests like delete /requests/5
    public void deleteRequest(@PathVariable Long id) {
        helpRequestService.deleteRequest(id);
    }
}
