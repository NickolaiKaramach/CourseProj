package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.AccessRole;
import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.UserCreationService;
import by.bsuir.karamach.serviceworker.logic.validator.CustomerInfoValidator;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegisterService implements UserCreationService {

    private CustomerRepository repository;

    public RegisterService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(Customer customer) throws ServiceException {
        if (CustomerInfoValidator.isValidCustomer(customer)) {

            Customer alreadyRegisteredCustomer = repository.findByEmail(customer.getEmail());

            if (alreadyRegisteredCustomer == null) {

                customer.setRole(Collections.singleton(AccessRole.USER));
                repository.save(customer);

            } else {
                throw new ServiceException("Email is already taken!");
            }
        } else {
            throw new ServiceException("Invalid data input!");
        }
    }

}
