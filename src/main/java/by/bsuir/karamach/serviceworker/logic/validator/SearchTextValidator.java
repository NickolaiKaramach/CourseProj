package by.bsuir.karamach.serviceworker.logic.validator;

public class SearchTextValidator {

    private static final int FIRST_PAGE = 0;

    public static boolean isValidTextForSearch(String text) {
        //We can add filters
        return (text != null) &&
                !text.isEmpty();
    }

    public static boolean isValidPageNum(int page) {
        return page >= FIRST_PAGE;
    }
}
