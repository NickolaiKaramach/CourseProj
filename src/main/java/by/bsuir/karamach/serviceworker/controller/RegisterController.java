package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.ErrorResponse;
import by.bsuir.karamach.serviceworker.entity.PositiveResponse;
import by.bsuir.karamach.serviceworker.entity.RegistrationRequest;
import by.bsuir.karamach.serviceworker.entity.RegistrationRequestResponse;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.RegisterService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    private static final String REGISTER_STATUS_OK = "Successfully registered new student!";
    private static final int EXPIRATION_TIME = 5400;
    private static final String ACTIVATE_STATUS_OK = "Successfully activated!";
    private static final String ACTIVATE_STATUS_BAD = "Couldn't find activate code";
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
        try {
            registerService.activateUser(code, publicId);
        } catch (ServiceException e) {
            message = e.getMessage();
        }

        PositiveResponse positiveResponse = new PositiveResponse(IS_SUCCESSFUL);
        ErrorResponse errorResponse = new ErrorResponse(IS_NOT_SUCCESSFUL, message);

        return (message == null) ? positiveResponse : errorResponse;
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
