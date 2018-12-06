package by.bsuir.karamach.serviceworker.logic.validator;

import by.bsuir.karamach.serviceworker.entity.Customer;

import java.util.Calendar;

public class CustomerInfoValidator {
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final int MIN_BIRTH_YEAR = 1960;

    public static boolean isValidEmail(String email) {
        return (
                isNotNullAndNotEmptyString(email) &&
                        (email.toUpperCase().matches(EMAIL_REGEX))
        );
    }

    public static boolean isValidPassword(String password) {
        return isNotNullAndNotEmptyString(password);
    }

    private static boolean isNotNullAndNotEmptyString(String text) {
        return (text != null) &&
                (!text.isEmpty());
    }

    public static boolean isValidBirthYear(int year) {
        Calendar calendar = Calendar.getInstance();

        return (MIN_BIRTH_YEAR < year) && (year <= calendar.get(Calendar.YEAR));
    }

    public static boolean isValidCustomer(Customer customer) {
        if (customer != null) {
            String email = customer.getEmail();

            String firstName = customer.getFirstName();
            String lastName = customer.getLastName();

            String hashedPass = customer.getHashedPass();

            int birthYear = customer.getBirthYear();

            return ((((isValidEmail(email) &&
                    isValidPassword(hashedPass)) &&
                    isNotNullAndNotEmptyString(firstName)) &&
                    isNotNullAndNotEmptyString(lastName)) &&
                    isValidBirthYear(birthYear)
            );
        } else {
            return false;
        }
    }
}
