package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.SearchResponse;
import by.bsuir.karamach.serviceworker.entity.Trainer;
import by.bsuir.karamach.serviceworker.logic.SearchService;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/search")
public class SearchController {


    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public SearchResponse getTrainerOnlyByTextAndPage(String text, int pageNum) {
        String message;
        List<Trainer> trainers;

        try {
            trainers = searchService.getTrainerOnPageByText(text, pageNum);
            if (trainers.size() == 0) {
                message = "Нет репетиторов, удволетворяющих поиску!";
            } else {
                message = "Результатов на странице: " + trainers.size() + "!";
            }
        } catch (ServiceException e) {
            //TODO: LOG !
            message = e.getMessage();
            trainers = null;
        }


        return new SearchResponse(trainers, message, pageNum);
    }
}
