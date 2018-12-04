package by.bsuir.karamach.serviceworker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {


    @PostMapping(path = "/student")
    public String addNewStudent(String email, String password, String lastName,
                                String firstName, boolean isFemale, int birthYear) {


        return "Saved successfully!";
    }
}
