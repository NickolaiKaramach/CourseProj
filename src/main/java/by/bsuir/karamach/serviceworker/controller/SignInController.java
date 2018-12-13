package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.entity.ErrorResponse;
import by.bsuir.karamach.serviceworker.entity.LogInResponse;
import by.bsuir.karamach.serviceworker.entity.LoginInfo;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.CustomerService;
import by.bsuir.karamach.serviceworker.logic.impl.SignInService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignInController {

    private static final String STATUS_OK = "Successfully signed in!";
    private static final String STATUS_BAD = "Invalid combination of email and password!";


    private SignInService signInService;

    private CustomerService customerService;

    public SignInController(SignInService signInService, CustomerService customerService) {
        this.signInService = signInService;
        this.customerService = customerService;
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


                jwtToken = customerService.renewCustomerJWTToken(customer);

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

        LogInResponse logInResponse = new LogInResponse(isSuccessful, jwtToken, customer);
        
        return isSuccessful ? logInResponse : errorResponse;
    }

}
