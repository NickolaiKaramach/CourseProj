package by.bsuir.karamach.serviceworker.repository;

import by.bsuir.karamach.serviceworker.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByNameAndPassword(String name, String password);

}
