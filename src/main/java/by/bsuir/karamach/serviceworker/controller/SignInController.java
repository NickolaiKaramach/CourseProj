package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
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
@RequestMapping(path = "/login")
public class SignInController {

    private static final String STATUS_OK = "Successfully signed in!";
    private static final String STATUS_BAD = "Invalid combination of email and password!";
    private static final int EXPIRATION_TIME = 5400;
    private static final int EXPIRATION_TIME_NOW = 0;
    private static final String EMPTY_VALUE = "";
    private static final String EMPTY_PATH = "/";
    private static final String LOG_OUT_STATUS_OK = "Successfully logged out!";

    private SignInService signInService;

    private CustomerRepository customerRepository;

    private SecurityHelper securityHelper;


    public SignInController(SignInService signInService, CustomerRepository customerRepository,
                            SecurityHelper securityHelper) {
        this.signInService = signInService;
        this.customerRepository = customerRepository;
        this.securityHelper = securityHelper;
    }

    @PostMapping
    public String signIn(String email, String hashedPassword,
                         HttpServletRequest req, HttpServletResponse resp) {
        String msg;

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
            }
        } catch (ServiceException e) {
            //TODO: Log !
            msg = e.getMessage();
        }

        return msg;
    }

    @PostMapping(path = "/logout")
    public String logOut(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();

        String message = LOG_OUT_STATUS_OK;

        if ((cookies != null) && (cookies.length > 0)) {

            for (Cookie cookie : cookies) {

                if (cookie.getName().equals(TOKEN)) {

                    message = doLogOut(resp, message, cookie);
                }
            }
        }

        return message;
    }

    private String doLogOut(HttpServletResponse resp, String message, Cookie cookie) {
        try {
            signInService.logOut(cookie.getValue());

            cookie.setValue(EMPTY_VALUE);
            cookie.setPath(EMPTY_PATH);
            cookie.setMaxAge(EXPIRATION_TIME_NOW);

            resp.addCookie(cookie);

        } catch (ServiceException e) {
            message = e.getMessage();
        }
        return message;
    }
}
