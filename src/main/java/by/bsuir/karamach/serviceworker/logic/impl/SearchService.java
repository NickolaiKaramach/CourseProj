package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.Customer;
import by.bsuir.karamach.serviceworker.entity.SearchResponse;
import by.bsuir.karamach.serviceworker.entity.Subject;
import by.bsuir.karamach.serviceworker.entity.Trainer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.validator.SearchTextValidator;
import by.bsuir.karamach.serviceworker.repository.SubjectRepository;
import by.bsuir.karamach.serviceworker.repository.TrainerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        SearchResponse searchResponse = null;

        List<Trainer> trainers = new ArrayList<>();
        int totalCount = 0;

        boolean isValidData = isValidText && isValidPage;
        if (!isValidData) {
            throw new ServiceException("Некорректные данные!");
        }

        Subject subject = subjectRepository.findByNameContains(searchingText);

        if (subject != null) {


            totalCount = subject.getTrainers().size();

            trainers =
                    trainerRepository.getTrainersByActiveIsTrueAndSubjectEquals
                            (subject, PageRequest.of(page, PAGE_SIZE));


        } else {
            List<Trainer> tempTrainers = trainerRepository.findAllByActiveIsTrue();

            for (Trainer trainer : tempTrainers) {

                Customer customerData = trainer.getCustomer();

                String lastName = customerData.getLastName().toUpperCase();
                String firstName = customerData.getFirstName().toUpperCase();

                if ((lastName.contains(searchingText)) || (firstName.contains(searchingText))) {
                    if ((totalCount < PAGE_SIZE * (page + 1)) && (PAGE_SIZE * page <= totalCount)) {
                        trainers.add(trainer);
                    }
                    totalCount++;
                }
            }
        }

        searchResponse = new SearchResponse();
        searchResponse.setTrainersFound(trainers);
        searchResponse.setPage(page);

        searchResponse.setTotalCount(totalCount);

        return searchResponse;
    }
}
