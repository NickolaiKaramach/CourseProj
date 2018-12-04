package by.bsuir.karamach.serviceworker.logic;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.logic.validator.CustomerInfoValidator;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

    private CustomerRepository customerRepository;

    public SignInService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public int getAccessLevel(String email, String password) throws ServiceException {
        int accessLevel = 0;

        if (CustomerInfoValidator.isValidEmail(email) &&
                CustomerInfoValidator.isValidPassword(password)) {

            Customer customer;
            customer = customerRepository.findByEmailAndPassword(email, password);

            if (customer != null) {
                accessLevel = customer.getAccessLevel();
            }
        } else {
            throw new ServiceException("Данные некорректны!");
        }

        return accessLevel;
    }
}
