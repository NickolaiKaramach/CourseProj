package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.*;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.CustomerService;
import by.bsuir.karamach.serviceworker.logic.impl.SignInService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping(path = "/logout")
    public Object doLogout(HttpServletRequest req) {

        String token = req.getHeader("Authorization");

        Object response;
        boolean status;

        try {
            status = signInService.logOut(token);
        } catch (ServiceException e) {
            status = false;
            //TODO: LOG !
        }

        if (status) {
            response = new PositiveResponse(true);
        } else {
            response = new ErrorResponse(false, "Cannot find current session!");
        }

        return response;

    }

}
