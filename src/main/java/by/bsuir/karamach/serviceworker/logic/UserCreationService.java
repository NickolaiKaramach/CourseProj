package by.bsuir.karamach.serviceworker.logic;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.entity.RegistrationRequest;

public interface UserCreationService {

    void createRegistrationRequest(RegistrationRequest registrationRequest) throws ServiceException;

    Customer activateUser(String code, String publicId) throws ServiceException;
}
