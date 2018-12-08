package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.AccessRole;
import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.UserCreationService;
import by.bsuir.karamach.serviceworker.logic.validator.CustomerInfoValidator;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegisterService implements UserCreationService {

    private static final String ACTIVATION_URL = "http://localhost:8080/activate/";
    private static final String MESSAGE_TO_USER = "   Hello, %s! \n" +
            "Welcome to our platform, \n" +
            "To activate your account, visit this link: \n" +
            ACTIVATION_URL + "%s";
    private static final String ACCOUNT_ACTIVATION = "Account activation";

    private SecurityHelper securityHelper;

    private CustomerRepository repository;

    private MailSender sender;

    public RegisterService(CustomerRepository repository, SecurityHelper securityHelper,
                           MailSender sender) {
        this.sender = sender;
        this.repository = repository;
        this.securityHelper = securityHelper;
    }

    @Override
    public void createUser(Customer customer) throws ServiceException {
        if (CustomerInfoValidator.isValidCustomer(customer)) {

            Customer alreadyRegisteredCustomer = repository.findByEmail(customer.getEmail());

            if (alreadyRegisteredCustomer == null) {

                customer.setRole(Collections.singleton(AccessRole.USER));
                customer.setActivationCode(securityHelper.generateActivationCode());

                repository.save(customer);

                String message = String.format(MESSAGE_TO_USER,
                        customer.getFirstName(), customer.getActivationCode());

                sender.send(customer.getEmail(), ACCOUNT_ACTIVATION, message);

            } else {
                throw new ServiceException("Email is already taken!");
            }
        } else {
            throw new ServiceException("Invalid data input!");
        }
    }

    @Override
    public boolean activateUser(String code) {
        Customer customer = repository.findByActivationCode(code);

        if (customer == null) {
            return false;
        }

        customer.setActivationCode(null);

        repository.save(customer);

        return true;
    }
}
