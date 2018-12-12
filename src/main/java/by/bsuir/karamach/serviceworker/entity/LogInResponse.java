package by.bsuir.karamach.serviceworker.entity;

public class LogInResponse {
    private boolean isSuccessful;
    private String token;
    private Customer customer;

    public LogInResponse() {
    }

    public LogInResponse(boolean isSuccessful, String token, Customer customer) {
        this.isSuccessful = isSuccessful;
        this.token = token;
        this.customer = customer;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}