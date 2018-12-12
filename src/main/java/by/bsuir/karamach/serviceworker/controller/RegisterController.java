package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.*;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.RegisterService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterController {

    private static final String REGISTER_STATUS_OK = "Successfully registered new student!";
    private static final boolean IS_SUCCESSFUL = true;
    private static final boolean IS_NOT_SUCCESSFUL = false;
    private RegisterService registerService;

    private SecurityHelper securityHelper;

    private CustomerRepository customerRepository;

    public RegisterController(RegisterService registerService, SecurityHelper securityHelper,
                              CustomerRepository customerRepository) {
        this.registerService = registerService;
        this.customerRepository = customerRepository;
        this.securityHelper = securityHelper;
    }

    @PostMapping(path = "/activate")
    public Object activate(@RequestBody ActivationDetails activationDetails) {

        String message = null;

        String publicId = activationDetails.publicId;
        String code = activationDetails.code;

        Customer customer = null;

        try {
            customer = registerService.activateUser(code, publicId);
        } catch (ServiceException e) {
            message = e.getMessage();
        }

        Object response;

        if (customer != null) {
            Map<String, String> tokenValues = new HashMap<>();

            tokenValues.put("email", customer.getEmail());
            tokenValues.put("password", customer.getHashedPass());

            String jwtToken = securityHelper.generateJWTToken(tokenValues);
            customer.setTempToken(jwtToken);
            customerRepository.save(customer);

            response = new LogInResponse(IS_SUCCESSFUL, jwtToken, customer);
        } else {
            response = new ErrorResponse(IS_NOT_SUCCESSFUL, message);
        }


        return response;
    }

    @PostMapping(path = "/register/customer")
    public Object addNewStudent(@RequestBody RegistrationRequest registrationRequest) {

        boolean isSuccessfully = true;
        String message = REGISTER_STATUS_OK;

        try {
            registerService.createRegistrationRequest(registrationRequest);
        } catch (ServiceException e) {
            isSuccessfully = false;
            message = e.getMessage();
        }


        String generatedPublicId = registrationRequest.getGeneratedPublicId();

        Object positiveResponse = new RegistrationRequestResponse(isSuccessfully, generatedPublicId);
        Object errorResponse = new ErrorResponse(isSuccessfully, message);

        return isSuccessfully ? positiveResponse : errorResponse;
    }

    public static final class ActivationDetails {
        public String publicId;
        public String code;
    }

}
