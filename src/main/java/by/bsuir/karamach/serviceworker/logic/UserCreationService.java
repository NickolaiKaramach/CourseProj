package by.bsuir.karamach.serviceworker.logic;

import by.bsuir.karamach.serviceworker.entity.Customer;

public interface UserCreationService {
    void createUser(Customer customer) throws ServiceException;

    boolean activateUser(String code);
}
