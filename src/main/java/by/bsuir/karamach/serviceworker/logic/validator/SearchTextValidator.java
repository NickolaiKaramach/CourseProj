package by.bsuir.karamach.serviceworker.logic.validator;

public class SearchTextValidator {
    public static boolean isValidTextForSearch(String text) {
        //We can add filters
        return (text != null) &&
                !text.isEmpty();
    }
}
