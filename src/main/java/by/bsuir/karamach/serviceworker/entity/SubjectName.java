package by.bsuir.karamach.serviceworker.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectName {
    private static final List<String> subjectNames = new ArrayList<>();

    public SubjectName() {
        subjectNames.add("МАТЕМАТИКА");
        subjectNames.add("РУССКИЙ");
        subjectNames.add("АНГЛИЙСКИЙ");
    }

    public boolean isInSubjectList(String subjectName) {
        return subjectNames.contains(subjectName);
    }
}
