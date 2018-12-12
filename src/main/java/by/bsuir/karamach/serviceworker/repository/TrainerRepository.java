package by.bsuir.karamach.serviceworker.repository;


import by.bsuir.karamach.serviceworker.entity.Subject;
import by.bsuir.karamach.serviceworker.entity.Trainer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface TrainerRepository extends PagingAndSortingRepository<Trainer, Integer> {

    List<Trainer> getTrainersByActiveIsTrueAndSubjectEquals(Subject subject, Pageable pageable);

    int countAllByActiveIsTrueAndSubjectContains(Subject subject);

    Integer countAllByActiveIsTrue();
}
