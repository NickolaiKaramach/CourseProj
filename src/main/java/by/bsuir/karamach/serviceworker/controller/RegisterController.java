package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.*;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.CustomerService;
import by.bsuir.karamach.serviceworker.logic.impl.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private static final String REGISTER_STATUS_OK = "Successfully registered new student!";
    private static final boolean IS_SUCCESSFUL = true;
    private static final boolean IS_NOT_SUCCESSFUL = false;

    private RegisterService registerService;

    private CustomerService customerService;


    public RegisterController(RegisterService registerService, CustomerService customerService) {
        this.registerService = registerService;
        this.customerService = customerService;
    }


    @PostMapping(path = "/activate")
    public Object activate(@RequestBody ActivationDetails activationDetails) {

        String message = null;

        String publicId = activationDetails.getPublicId();
        String code = activationDetails.getCode();

        Customer customer = null;

        try {
            customer = registerService.activateUser(code, publicId);
        } catch (ServiceException e) {
            message = e.getMessage();
        }

        Object response;

        if (customer != null) {

            String jwtToken = customerService.renewCustomerJWTToken(customer);

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
        String publicId;
        String code;

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
