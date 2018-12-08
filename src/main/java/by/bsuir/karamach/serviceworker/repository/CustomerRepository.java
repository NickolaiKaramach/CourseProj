package by.bsuir.karamach.serviceworker.repository;

import by.bsuir.karamach.serviceworker.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer findByEmailAndHashedPass(String email, String hashedPass);

    Customer findByTempToken(String tempToken);

    Customer findByEmail(String email);

    Customer findByActivationCode(String code);
}
