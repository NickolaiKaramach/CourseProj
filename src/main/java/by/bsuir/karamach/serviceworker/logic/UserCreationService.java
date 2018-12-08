package by.bsuir.karamach.serviceworker.logic;

import by.bsuir.karamach.serviceworker.entity.RegistrationRequest;

public interface UserCreationService {

    void createRegistrationRequest(RegistrationRequest registrationRequest) throws ServiceException;

    void activateUser(String code, String publicId) throws ServiceException;
}
