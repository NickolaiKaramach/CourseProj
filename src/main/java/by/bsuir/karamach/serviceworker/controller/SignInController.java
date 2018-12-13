package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.entity.ErrorResponse;
import by.bsuir.karamach.serviceworker.entity.LogInResponse;
import by.bsuir.karamach.serviceworker.entity.LoginInfo;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.SignInService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SignInController {

    private static final String STATUS_OK = "Successfully signed in!";
    private static final String STATUS_BAD = "Invalid combination of email and password!";
    

    private SignInService signInService;

    private CustomerRepository customerRepository;

    private SecurityHelper securityHelper;


    public SignInController(SignInService signInService, CustomerRepository customerRepository,
                            SecurityHelper securityHelper) {
        this.signInService = signInService;
        this.customerRepository = customerRepository;
        this.securityHelper = securityHelper;
    }



    @PostMapping(path = "/login")
    public Object signIn(@RequestBody LoginInfo loginInfo) {
        String msg;
        String jwtToken = null;

        String email = loginInfo.getEmail();
        String hashedPassword = loginInfo.getHashedPassword();

        Customer customer = null;

        boolean isSuccessful = true;

        try {
            customer = signInService.logIn(email, hashedPassword);


            if (customer != null) {
                msg = STATUS_OK;


                Map<String, String> tokenValues = new HashMap<>();

                tokenValues.put("email", customer.getEmail());
                tokenValues.put("password", customer.getHashedPass());

                jwtToken = securityHelper.generateJWTToken(tokenValues);

                customer.setTempToken(jwtToken);
                customerRepository.save(customer);

            } else {
                msg = STATUS_BAD;
                isSuccessful = false;
            }
        } catch (ServiceException e) {
            //TODO: Log !
            msg = e.getMessage();
            isSuccessful = false;
        }

        ErrorResponse errorResponse = new ErrorResponse(isSuccessful, msg);

        return isSuccessful ? new LogInResponse(isSuccessful, jwtToken, customer) : errorResponse;
    }

}
