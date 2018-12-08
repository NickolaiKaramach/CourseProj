package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.ErrorResponse;
import by.bsuir.karamach.serviceworker.entity.PositiveResponse;
import by.bsuir.karamach.serviceworker.entity.RegistrationRequest;
import by.bsuir.karamach.serviceworker.entity.RegistrationRequestResponse;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.RegisterService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    @GetMapping(path = "/activate/{code}")
    public Object activate(@PathVariable String code, String publicId) {

        String message = null;

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
    public Object addNewStudent(String email, String hashedPassword, String lastName,
                                String firstName, boolean isFemale, int birthYear,
                                HttpServletResponse resp) {

        boolean isSuccessfully = true;
        String message = REGISTER_STATUS_OK;

        RegistrationRequest request = getRegistrationRequestFromData
                (email, hashedPassword, lastName,
                        firstName, isFemale, birthYear);


        try {
            registerService.createRegistrationRequest(request);
        } catch (ServiceException e) {
            isSuccessfully = false;
            message = e.getMessage();
        }


        Object positiveResponse = new RegistrationRequestResponse(isSuccessfully, request.getGeneratedPublicId());
        Object errorResponse = new ErrorResponse(isSuccessfully, message);

        return isSuccessfully ? positiveResponse : errorResponse;
    }

    private RegistrationRequest getRegistrationRequestFromData
            (String email, String hashedPassword, String lastName,
             String firstName, boolean isFemale, int birthYear) {

        RegistrationRequest registrationRequest = new RegistrationRequest();

        registrationRequest.setEmail(email);
        registrationRequest.setHashedPass(hashedPassword);

        registrationRequest.setLastName(lastName);
        registrationRequest.setFirstName(firstName);
        registrationRequest.setFemale(isFemale);
        registrationRequest.setBirthYear(birthYear);

        registrationRequest.setActivationCode(securityHelper.generateActivationCode());
        registrationRequest.setGeneratedPublicId(securityHelper.generatePublicId());

        return registrationRequest;
    }
}
