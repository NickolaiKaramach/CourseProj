package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.SearchResponse;
import by.bsuir.karamach.serviceworker.entity.Trainer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/search")
public class SearchController {


    private static final int NO_TRAINERS_FOUND = 0;
    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public SearchResponse
    getTrainerOnlyByTextAndPage(String text,
                                @RequestParam(required = false, defaultValue = "0") int page) {
        String message;
        List<Trainer> trainers;

        SearchResponse searchResponse;

        try {
            searchResponse = searchService.getTrainerOnPageByText(text, page);

            List<Trainer> trainersFound = searchResponse.getTrainersFound();

            if ((trainersFound == null) || (trainersFound.size() == 0)) {
                message = "Нет репетиторов, удволетворяющих поиску!";
            } else {
                message = "Найдено: " + trainersFound.size() + " репетиторов!";
            }
        } catch (ServiceException e) {
            //TODO: LOG !
            message = e.getMessage();
            trainers = null;

            searchResponse = new SearchResponse();
            searchResponse.setTrainersFound(trainers);
            searchResponse.setTotalCount(NO_TRAINERS_FOUND);
            searchResponse.setPage(page);
        }

        searchResponse.setMessage(message);

        return searchResponse;
    }
}
