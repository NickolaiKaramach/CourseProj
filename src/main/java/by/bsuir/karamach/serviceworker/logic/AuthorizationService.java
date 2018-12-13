package by.bsuir.karamach.serviceworker.logic;

import by.bsuir.karamach.serviceworker.entity.Customer;

public interface AuthorizationService {
    Customer logIn(String email, String hashedPassword) throws ServiceException;

    boolean logOut(String tempToken) throws ServiceException;
}
