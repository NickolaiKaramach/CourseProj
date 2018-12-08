package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.entity.ErrorResponse;
import by.bsuir.karamach.serviceworker.entity.PositiveResponse;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.MailSender;
import by.bsuir.karamach.serviceworker.logic.impl.SignInService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static by.bsuir.karamach.serviceworker.controller.CookieParameterName.TOKEN;

@RestController
public class SignInController {

    private static final String STATUS_OK = "Successfully signed in!";
    private static final String STATUS_BAD = "Invalid combination of email and password!";
    private static final int EXPIRATION_TIME = 5400;
    private static final int EXPIRATION_TIME_NOW = 0;
    private static final String EMPTY_VALUE = "";
    private static final String EMPTY_PATH = "/";
    private static final String LOG_OUT_STATUS_OK = "Successfully logged out!";
    private static final String STATUS_TO_BE_ACTIVATED = "Please activate ur account first, one more message will be sent!";
    private static final String ACTIVATION_URL = "http://localhost:8080/activate/";
    private static final String MESSAGE_TO_USER = "   Hello, %s! \n" +
            "Welcome to our platform, \n" +
            "To activate your account, visit this link: \n" +
            ACTIVATION_URL + "%s";
    private static final String ACCOUNT_ACTIVATION = "Account activation";
    private static final String NOT_LOGGED_IN = "You don't have been logged in!";


    private SignInService signInService;

    private CustomerRepository customerRepository;

    private SecurityHelper securityHelper;

    private MailSender mailSender;

    public SignInController(SignInService signInService, CustomerRepository customerRepository,
                            SecurityHelper securityHelper, MailSender mailSender) {
        this.signInService = signInService;
        this.customerRepository = customerRepository;
        this.securityHelper = securityHelper;
        this.mailSender = mailSender;
    }


    @PostMapping(path = "/login")
    public Object signIn(String email, String hashedPassword,
                         HttpServletResponse resp) {
        String msg;

        boolean isSuccessful = true;

        try {
            Customer customer = signInService.logIn(email, hashedPassword);


            if (customer != null) {
                msg = STATUS_OK;

                String token = securityHelper.generateTempToken();

                customer.setTempToken(token);
                customerRepository.save(customer);

                Cookie tokenCookie = new Cookie(TOKEN, token);
                tokenCookie.setMaxAge(EXPIRATION_TIME);
                resp.addCookie(tokenCookie);

            } else {
                msg = STATUS_BAD;
                isSuccessful = false;
            }
        } catch (ServiceException e) {
            //TODO: Log !
            msg = e.getMessage();
            isSuccessful = false;
        }

        PositiveResponse positiveResponse = new PositiveResponse(isSuccessful);
        ErrorResponse errorResponse = new ErrorResponse(isSuccessful, msg);

        return isSuccessful ? positiveResponse : errorResponse;
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
