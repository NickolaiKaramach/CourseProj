package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.entity.ErrorResponse;
import by.bsuir.karamach.serviceworker.entity.SearchResponse;
import by.bsuir.karamach.serviceworker.entity.Trainer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.impl.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/trainers")
public class SearchController {


    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public Object
    getTrainerOnlyByTextAndPage(String text, int page) {
        String message;

        SearchResponse searchResponse;
        Object response;

        try {
            searchResponse = searchService.getTrainerOnPageByText(text, page);

            List<Trainer> trainersFound = searchResponse.getTrainersFound();


            boolean isNoTrainersAvailable =
                    (trainersFound == null) || (trainersFound.size() == 0);

            if (isNoTrainersAvailable) {

                message = "Нет репетиторов, удволетворяющих поиску!";

            } else {

                message = "Найдено: " + searchResponse.getTotalCount() + " репетиторов!";

            }


            searchResponse.setMessage(message);
            response = searchResponse;
        } catch (ServiceException e) {
            //TODO: LOG !

            response = new ErrorResponse(false, e.getMessage());
        }

        return response;
    }
}
