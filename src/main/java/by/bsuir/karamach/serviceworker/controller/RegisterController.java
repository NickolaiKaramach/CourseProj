package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.RegisterService;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {

    private static final String REGISTER_STATUS_OK = "Successfully registered new student!";
    private static final int EXPIRATION_TIME = 5400;
    private RegisterService registerService;

    private SecurityHelper securityHelper;

    private CustomerRepository customerRepository;

    public RegisterController(RegisterService registerService, SecurityHelper securityHelper,
                              CustomerRepository customerRepository) {
        this.registerService = registerService;
        this.customerRepository = customerRepository;
        this.securityHelper = securityHelper;
    }

    @PostMapping(path = "/customer")
    public String addNewStudent(String email, String hashedPassword, String lastName,
                                String firstName, boolean isFemale, int birthYear,
                                HttpServletResponse resp) {

        String message = REGISTER_STATUS_OK;

        Customer customer = getCustomerFromData
                (email, hashedPassword, lastName,
                        firstName, isFemale, birthYear);

        try {
            registerService.createUser(customer);

        } catch (ServiceException e) {
            message = e.getMessage();
        }


        return message;
    }

    private Customer getCustomerFromData(String email, String hashedPassword, String lastName, String firstName, boolean isFemale, int birthYear) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setHashedPass(hashedPassword);
        customer.setLastName(lastName);
        customer.setFirstName(firstName);
        customer.setFemale(isFemale);
        customer.setBirthYear(birthYear);
        return customer;
    }
}
