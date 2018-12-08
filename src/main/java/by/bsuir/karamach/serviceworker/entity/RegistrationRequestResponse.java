package by.bsuir.karamach.serviceworker.entity;

public class RegistrationRequestResponse {
    private boolean isSuccessful;

    private String publicId;

    public RegistrationRequestResponse() {
    }

    public RegistrationRequestResponse(boolean isSuccessful, String publicId) {
        this.isSuccessful = isSuccessful;
        this.publicId = publicId;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
