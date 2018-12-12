package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.*;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.SignInService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static by.bsuir.karamach.serviceworker.controller.CookieParameterName.TOKEN;

@RestController
public class SignInController {

    private static final String STATUS_OK = "Successfully signed in!";
    private static final String STATUS_BAD = "Invalid combination of email and password!";
    private static final int EXPIRATION_TIME_NOW = 0;
    private static final String EMPTY_VALUE = "";
    private static final String EMPTY_PATH = "/";
    private static final String LOG_OUT_STATUS_OK = "Successfully logged out!";
    private static final String NOT_LOGGED_IN = "You don't have been logged in!";


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

    @RequestMapping(path = "/logout")
    public Object logOut(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();

        String message = LOG_OUT_STATUS_OK;
        boolean isSuccessful = true;

        try {
            if ((cookies != null) && (cookies.length > 0)) {

                for (Cookie cookie : cookies) {

                    if (cookie.getName().equals(TOKEN)) {
                        doLogOut(resp, cookie);
                    }
                }
            } else {
                isSuccessful = false;
                message = NOT_LOGGED_IN;
            }
        } catch (ServiceException e) {
            isSuccessful = false;
            message = e.getMessage();
        }

        PositiveResponse positiveResponse = new PositiveResponse(isSuccessful);
        ErrorResponse errorResponse = new ErrorResponse(isSuccessful, message);

        return isSuccessful ? positiveResponse : errorResponse;
    }

    private void doLogOut(HttpServletResponse resp, Cookie cookie) throws ServiceException {

        signInService.logOut(cookie.getValue());

        cookie.setValue(EMPTY_VALUE);
        cookie.setPath(EMPTY_PATH);
        cookie.setMaxAge(EXPIRATION_TIME_NOW);

        resp.addCookie(cookie);
    }
}
