package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.logic.AuthorizationService;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.validator.CustomerInfoValidator;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class SignInService implements AuthorizationService {

    private static final String EXPIRED_TEMP_TOKEN_FLAG = "EXPIRED";
    private CustomerRepository customerRepository;

    public SignInService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer logIn(String email, String hashedPassword) throws ServiceException {
        boolean isValidData = isValidData(email, hashedPassword);

        Customer customer = null;

        if (isValidData) {
            customer =
                    customerRepository.findByEmailAndHashedPass(email, hashedPassword);
        }

        return customer;
    }

    private boolean isValidData(String email, String hashedPassword) {
        boolean isValidPassword = CustomerInfoValidator.isValidPassword(hashedPassword);
        boolean isValidEmail = CustomerInfoValidator.isValidEmail(email);
        return isValidPassword && isValidEmail;
    }

    @Override
    public boolean logOut(String tempToken) throws ServiceException {
        Customer customer = customerRepository.findByTempToken(tempToken);
        boolean status;

        if (customer != null) {
            status = true;
            customer.setTempToken(EXPIRED_TEMP_TOKEN_FLAG);
            customerRepository.save(customer);
        } else {
            status = false;
            //TODO: LOG !
        }

        return status;
    }
}
