package guardianlink.controller;

import guardianlink.dto.CreateHelpRequestDto;
import guardianlink.dto.HelpRequestResponse;
import guardianlink.model.HelpRequest;
import guardianlink.security.JwtUtil;
import guardianlink.service.HelpRequestService;
import guardianlink.util.ApiResponse;
import org.springframework.security.core.Authentication;
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
    @PostMapping
    public ApiResponse<HelpRequestResponse> createRequest(
            Authentication authentication,
            @Valid @RequestBody CreateHelpRequestDto dto
    ) {
        String email = authentication.getName();

        HelpRequestResponse response =
                helpRequestService.createRequest(
                        dto.getName(),
                        dto.getCategoryId(),
                        email
                );

        return ApiResponse.success("Request created successfully", response);
    }

    // GET: all requests
     @GetMapping
        public ApiResponse<List<HelpRequestResponse>> getAllRequests(
                Authentication authentication
        ) {
            List<HelpRequestResponse> responses =
                    helpRequestService.getAllRequests();

            return ApiResponse.success("Requests fetched successfully", responses);
        }

    // GET: request by id
    @GetMapping("/{id}")
    public ApiResponse<HelpRequestResponse> getRequestById(
            @PathVariable Long id
    ) {
        HelpRequestResponse response =
                helpRequestService.getRequestById(id);

        return ApiResponse.success("Request fetched successfully", response);
    }

    // PUT: mark request as completed
    @PutMapping("/{id}/complete")
    public ApiResponse<HelpRequestResponse> completeRequest(
            Authentication authentication,
            @PathVariable Long id
    ) {
        String email = authentication.getName();

        HelpRequestResponse response =
                helpRequestService.completeRequest(id, email);

        return ApiResponse.success("Request completed successfully", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRequest(
            Authentication authentication,
            @PathVariable Long id
    ) {
        String email = authentication.getName();

        helpRequestService.deleteRequest(id, email);

        return ApiResponse.success("Request deleted successfully", null);
    }

    @GetMapping("/my")
    public ApiResponse<List<HelpRequestResponse>> getMyRequests(
            Authentication authentication
    ) {
        String email = authentication.getName();

        List<HelpRequestResponse> responses =
                helpRequestService.getRequestsForUser(email);

        return ApiResponse.success("Your requests fetched successfully", responses);
    }

    @GetMapping("/assigned") //returns only requests assigned to the volunteer
    public ApiResponse<List<HelpRequestResponse>> getAssignedRequests(Authentication authentication) {
        String email = authentication.getName();
        List<HelpRequestResponse> responses = helpRequestService.getAssignedRequests(email);
        return ApiResponse.success("Assigned requests fetched successfully", responses);
    }


    @PutMapping("/{id}/claim")
    public ApiResponse<HelpRequestResponse> claimRequest(
            Authentication authentication,
            @PathVariable Long id
    ) {
        String email = authentication.getName();

        HelpRequestResponse response =
                helpRequestService.claimRequest(id, email);

        return ApiResponse.success("Request claimed successfully", response);
    }
}
