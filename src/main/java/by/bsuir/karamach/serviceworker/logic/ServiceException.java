package by.bsuir.karamach.serviceworker.logic;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 1837208570607006911L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
