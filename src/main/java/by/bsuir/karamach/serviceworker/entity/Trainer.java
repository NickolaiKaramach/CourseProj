package by.bsuir.karamach.serviceworker.entity;

import javax.persistence.*;

@Entity
public class Trainer {

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Customer customer;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String mainSubjectName;

    private String additionalSubjectName;

    private String lessonType;

    private String city;

    private String district;

    private String telephoneNumber;

    private double avgPrice;

    private boolean active;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainSubjectName() {
        return mainSubjectName;
    }

    public void setMainSubjectName(String mainSubjectName) {
        this.mainSubjectName = mainSubjectName;
    }

    public String getAdditionalSubjectName() {
        return additionalSubjectName;
    }

    public void setAdditionalSubjectName(String additionalSubjectName) {
        this.additionalSubjectName = additionalSubjectName;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
