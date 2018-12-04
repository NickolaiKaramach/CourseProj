package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.LogInResponse;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.SignInService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
public class LogInController {


    private static final String DEFAULT_MESSAGE = "E R R O R";

    private SignInService signInService;

    public LogInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping
    public LogInResponse SignIn(String email, String password) {

        String msg = null;
        int accessLevel;

        try {
            accessLevel = signInService.getAccessLevel(email, password);

            switch (accessLevel) {
                case 0:
                    msg = "Нет такой комбинации email и пароля";
                    break;
                case 1:
                    msg = "Вы вошли в аккаунт как студент";
                    break;
                case 2:
                    msg = "Вы вошли в аккаунт как репетитор";
                    break;
            }
        } catch (ServiceException e) {
            //TODO: LOG !
            msg = DEFAULT_MESSAGE;
            accessLevel = -1;
        }

        return new LogInResponse(accessLevel, email, msg);
    }
}
