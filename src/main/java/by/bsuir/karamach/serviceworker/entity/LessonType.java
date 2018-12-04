package by.bsuir.karamach.serviceworker.entity;

import java.util.ArrayList;
import java.util.List;

public class LessonType {
    private List<String> lessonsTypes = new ArrayList<>();

    public LessonType() {
        lessonsTypes.add("У СТУДЕНТА");
        lessonsTypes.add("У РЕПЕТИТОРА");
        lessonsTypes.add("ОНЛАЙН");
    }
}
