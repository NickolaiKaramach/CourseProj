package by.bsuir.karamach.serviceworker.logic.validator;

public class CustomerInfoValidator {
    //private static final String EMAIL_REGEX = "\\\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}\\\\b";

    public static boolean isValidEmail(String email) {
        return
                (email != null) &&
                        (!email.isEmpty());
    }

    public static boolean isValidPassword(String password) {
        return (password != null) &&
                (!password.isEmpty());
    }
}
