package by.bsuir.karamach.serviceworker.repository;

import by.bsuir.karamach.serviceworker.entity.RegistrationRequest;
import org.springframework.data.repository.CrudRepository;

public interface RegistrationRequestRepository extends CrudRepository<RegistrationRequest, Integer> {
    RegistrationRequest findByActivationCode(String activationCode);
}
