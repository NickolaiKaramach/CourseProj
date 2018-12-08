package by.bsuir.karamach.serviceworker.entity;

public class PositiveResponse {
    private boolean isSuccessful;

    public PositiveResponse(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public PositiveResponse() {
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
