package org.egov.hrms.web.controller;

import org.egov.hrms.common.ApiResponse;
import org.egov.hrms.service.MsevaSsoService;
import org.egov.hrms.web.contract.AuthenticateUserInputRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employees/sso")
public class MsevaSsoWebController {
	
    @Autowired
    private MsevaSsoService msevaSsoService;
    
    
    @PostMapping("/authenticate-user")
    public String generateSSOUrl(@RequestBody AuthenticateUserInputRequest authenticateUserInputRequest) {
        
        // Map<String, Object> results =
        // msevaSsoService.fetchGenerateSsoUrlDetails(authenticateUserInputRequest);
        return msevaSsoService.generateSsoUrl(authenticateUserInputRequest);
    }

    @PostMapping("/authenticate-user1")
    public ApiResponse<?> generateSSOUrl1(@RequestBody AuthenticateUserInputRequest authenticateUserInputRequest) {
        
        try {

            return new ApiResponse<>(true, "fetch successfully",
                    msevaSsoService.fetchGenerateSsoUrlDetails(authenticateUserInputRequest));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse<>(false, "fetch failed." + ex.getMessage());
        }
        
    }
    @PostMapping("/authenticate-user11")
    public ResponseEntity<?> generateSSOUrl11(@RequestBody AuthenticateUserInputRequest authenticateUserInputRequest) {
        
        try {
            msevaSsoService.fetchGenerateSsoUrlDetails(authenticateUserInputRequest);
            return ResponseEntity.ok(msevaSsoService.fetchGenerateSsoUrlDetails(authenticateUserInputRequest));
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching data: " + ex.getMessage());
        }
        
    }
    
}
