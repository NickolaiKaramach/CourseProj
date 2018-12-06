package by.bsuir.karamach.serviceworker.logic.impl;

import by.bsuir.karamach.serviceworker.entity.Trainer;
import by.bsuir.karamach.serviceworker.logic.ServiceException;
import by.bsuir.karamach.serviceworker.logic.validator.SearchTextValidator;
import by.bsuir.karamach.serviceworker.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private static final int PAGE_SIZE = 2;
    private static final boolean IS_ACTIVE = true;

    private TrainerRepository trainerRepository;

    public SearchService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public List<Trainer> getTrainerOnPageByText(String searchingText, int pageNum) throws ServiceException {

        searchingText = searchingText.toUpperCase();

        boolean isValidText;
        isValidText = SearchTextValidator.isValidTextForSearch(searchingText);

        List<Trainer> trainers = null;

//        if ((isValidText) && (pageNum >= 0)) {
//
//            if (subject.isInSubjectList(searchingText)) {
//                trainers = trainerRepository.findByActiveIsTrueAndMainSubjectNameOrAdditionalSubjectNameContains
//                        (searchingText, searchingText, PageRequest.of(pageNum, PAGE_SIZE));
//
//            } else {
//                throw new ServiceException("Предмет не найден!");
//            }
//
//        } else {
//            throw new ServiceException("Некорректно введены данные!");
//        }
        return trainers;
    }
}
