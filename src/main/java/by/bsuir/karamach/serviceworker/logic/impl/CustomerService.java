package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.security.SecurityHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {
    private SecurityHelper securityHelper;
    private CustomerRepository customerRepository;

    public CustomerService(SecurityHelper securityHelper, CustomerRepository customerRepository) {
        this.securityHelper = securityHelper;
        this.customerRepository = customerRepository;
    }

    /**
     * @param customer - to add jwt-token to this one
     * @return jwt-token, have been generated
     */
    public String renewCustomerJWTToken(Customer customer) {
        Map<String, String> tokenValues = new HashMap<>();

        tokenValues.put("email", customer.getEmail());
        tokenValues.put("password", customer.getHashedPass());

        String jwtToken = securityHelper.generateJWTToken(tokenValues);
        customer.setTempToken(jwtToken);
        customerRepository.save(customer);

        return jwtToken;
    }
}
