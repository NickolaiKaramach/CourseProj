package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.SearchResponse;
import by.bsuir.karamach.serviceworker.entity.Subject;
import by.bsuir.karamach.serviceworker.entity.Trainer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.validator.SearchTextValidator;
import by.bsuir.karamach.serviceworker.repository.SubjectRepository;
import by.bsuir.karamach.serviceworker.repository.TrainerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private static final int PAGE_SIZE = 2;

    private TrainerRepository trainerRepository;

    private SubjectRepository subjectRepository;

    public SearchService(TrainerRepository trainerRepository, SubjectRepository subjectRepository) {
        this.trainerRepository = trainerRepository;
        this.subjectRepository = subjectRepository;
    }

    public SearchResponse getTrainerOnPageByText(String searchingText, int page) throws ServiceException {

        searchingText = searchingText.toUpperCase();

        boolean isValidPage;
        boolean isValidText;
        isValidPage = SearchTextValidator.isValidPageNum(page);
        isValidText = SearchTextValidator.isValidTextForSearch(searchingText);

        List<Trainer> trainers = null;
        int totalCount;

        Subject subject = subjectRepository.findByNameContaining(searchingText);

        if ((subject != null) && (isValidText && isValidPage)) {
            totalCount = trainerRepository.countAllByActiveIsTrueAndSubjectContains(subject);

            boolean isNotNullResult;
            isNotNullResult = (totalCount >= 0);

            if (isNotNullResult) {
                trainers =
                        trainerRepository.getTrainersByActiveIsTrueAndSubjectEquals
                                (subject, PageRequest.of(page, PAGE_SIZE));
            }
        } else {
            throw new ServiceException("Invalid data to search!");
        }


        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setTrainersFound(trainers);
        searchResponse.setPage(page);

        searchResponse.setTotalCount(totalCount);
        return searchResponse;
    }
}
