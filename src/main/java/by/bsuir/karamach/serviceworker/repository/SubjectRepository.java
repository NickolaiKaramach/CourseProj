package by.bsuir.karamach.serviceworker.repository;

import by.bsuir.karamach.serviceworker.entity.Subject;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Subject, Integer> {
    Subject findByNameContaining(String text);
}
