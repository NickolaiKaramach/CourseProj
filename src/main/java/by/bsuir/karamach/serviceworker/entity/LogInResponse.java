package by.bsuir.karamach.serviceworker.entity;

public class LogInResponse {
    private int accessLevel;
    private String email;
    private String message;

    public LogInResponse() {
    }

    public LogInResponse(int accessLevel, String email, String message) {
        this.accessLevel = accessLevel;
        this.email = email;
        this.message = message;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
