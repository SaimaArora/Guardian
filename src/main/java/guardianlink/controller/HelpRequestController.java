package guardianlink.controller;

import guardianlink.dto.CreateHelpRequestDto;
import guardianlink.model.HelpRequest;
import guardianlink.security.JwtUtil;
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
    public HelpRequest createRequest(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody CreateHelpRequestDto dto) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7).trim();
         //if header missing, spring return error, token invalid - request fails
        String email = JwtUtil.validateAndGetEmail(token); //throws if invalid
        return helpRequestService.createRequest(dto.getName(), dto.getCategoryId(), email);
        //we extract token from header, validate it, email from token and pass it to service instead of userid
    }

    private String extractEmailFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid authorization header");
        }
        String token = authHeader.substring(7).trim();
        return JwtUtil.validateAndGetEmail(token);
    }

    // GET: all requests
    @GetMapping
    public List<HelpRequest> getAllRequests(@RequestHeader("Authorization") String authHeader) {
        String email =extractEmailFromHeader(authHeader); //forces client to send jwt, validates token and blocks unauthenticated access
//        return helpRequestService.getRequestsForUser(email);
        return helpRequestService.getAllRequests();
    }

    // GET: request by id
    @GetMapping("/{id}")
    public HelpRequest getRequestById(@PathVariable Long id) {
        return helpRequestService.getRequestById(id);
    }

    // PUT: mark request as completed
    @PutMapping("/{id}/complete")
    public HelpRequest completeRequest(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        String token = authHeader.replace("Bearer ", "").trim();

        String email = JwtUtil.validateAndGetEmail(token);
        String role = JwtUtil.getRoleFromToken(token);
        if (!"VOLUNTEER".equals(role)) {
            throw new RuntimeException("Only volunteers can complete requests");
        }
        return helpRequestService.completeRequest(id, email);
    }

    @DeleteMapping("/{id}") //maps http delete requests like delete /requests/5
    public void deleteRequest(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        String email = extractEmailFromHeader(authHeader);
        helpRequestService.deleteRequest(id, email); //only logged in users can delete, jwt required, check ownership
    }

    @GetMapping("/my") //extract token, get email, fetch only that user's request
    public List<HelpRequest> getMyRequests(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        String email = JwtUtil.validateAndGetEmail(token);
        return helpRequestService.getRequestsForUser(email);
    }
}
