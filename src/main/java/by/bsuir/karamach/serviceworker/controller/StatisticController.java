package by.bsuir.karamach.serviceworker.controller;

import by.bsuir.karamach.serviceworker.repository.CustomerRepository;
import by.bsuir.karamach.serviceworker.repository.SubjectRepository;
import by.bsuir.karamach.serviceworker.repository.TrainerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticController {

    private CustomerRepository customerRepository;

    private SubjectRepository subjectRepository;

    private TrainerRepository trainerRepository;


    public StatisticController(CustomerRepository customerRepository, SubjectRepository subjectRepository, TrainerRepository trainerRepository) {
        this.customerRepository = customerRepository;
        this.subjectRepository = subjectRepository;
        this.trainerRepository = trainerRepository;
    }

    @GetMapping(path = "/statistic")
    public Object getStatisticData() {
        Integer allCustomers = customerRepository.countAllByEmailIsNotNull();
        Integer activeSubjects = subjectRepository.countAllByUsedIsTrue();
        Integer activeTrainers = trainerRepository.countAllByActiveIsTrue();

        Integer totalStudents = allCustomers - activeTrainers;

        return new StatisticData(activeTrainers, activeSubjects, totalStudents);
    }

    private final static class StatisticData {
        int totalTrainers;
        int activeSkills;
        int totalStudents;

        public StatisticData() {
        }

        public StatisticData(int totalTrainers, int activeSkills, int totalStudents) {
            this.totalTrainers = totalTrainers;
            this.activeSkills = activeSkills;
            this.totalStudents = totalStudents;
        }

        public int getTotalTrainers() {
            return totalTrainers;
        }

        public void setTotalTrainers(int totalTrainers) {
            this.totalTrainers = totalTrainers;
        }

        public int getActiveSkills() {
            return activeSkills;
        }

        public void setActiveSkills(int activeSkills) {
            this.activeSkills = activeSkills;
        }

        public int getTotalStudents() {
            return totalStudents;
        }

        public void setTotalStudents(int totalStudents) {
            this.totalStudents = totalStudents;
        }
    }
}
