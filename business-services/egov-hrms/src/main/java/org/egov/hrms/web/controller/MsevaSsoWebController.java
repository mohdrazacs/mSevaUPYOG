package org.egov.hrms.web.controller;

import java.util.Map;

import org.egov.hrms.common.ApiResponse;
import org.egov.hrms.service.MsevaSsoService;
import org.egov.hrms.web.contract.AuthenticateUserInputRequest;
import org.egov.hrms.web.contract.SsoResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping("/employees/sso")
public class MsevaSsoWebController {
	
    @Autowired
    private MsevaSsoService msevaSsoService;
    
    
    // @PostMapping("/authenticate-user")
    // public String generateSSOUrl(@RequestBody AuthenticateUserInputRequest authenticateUserInputRequest) {
        
    //     // Map<String, Object> results =
    //     // msevaSsoService.fetchGenerateSsoUrlDetails(authenticateUserInputRequest);
    //     return msevaSsoService.generateSsoUrl(authenticateUserInputRequest);
    // }

    @PostMapping("/authenticate-user")
    public ApiResponse<?> generateSSOUrl1(@RequestBody AuthenticateUserInputRequest authenticateUserInputRequest) {
            
        Map<String, Object> map = msevaSsoService.fetchGenerateSsoUrlDetails(authenticateUserInputRequest);
            if (map.get("code").equals(0)) {

                return new ApiResponse<>(false, (String)map.get("message")
                    );
            }
            else if (map.get("code").equals(2)) {

                return new ApiResponse<>(false, (String)map.get("message")
                    );
            }
            else if (map.get("code").equals(3)) {

                return new ApiResponse<>(false, (String)map.get("message")
                    );
            }
            else if (map.get("code").equals(4)) {

                return new ApiResponse<>(false, (String)map.get("message")
                    );
            }
            else if (map.get("code").equals(5)) {
                SsoResponce ssoResponce = new SsoResponce();
                ssoResponce.setUserName((String) map.get("userName"));
			    ssoResponce.setTenantId((String)map.get("tenantId"));
				ssoResponce.setEmployeeType((String)map.get("employeeType"));
				ssoResponce.setUrl((String) map.get("url"));
                return new ApiResponse<>(true, (String)map.get("message"),ssoResponce
                    );
            }else{
                return new ApiResponse<>(false, "fetch unsuccessfully",""
                    );
            } 
    } 
}
