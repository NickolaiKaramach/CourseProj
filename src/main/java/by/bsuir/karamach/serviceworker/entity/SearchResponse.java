package by.bsuir.karamach.serviceworker.entity;

import java.util.List;

public class SearchResponse {
    private List<Trainer> trainersFound;
    private String message;
    private int page;
    private int totalCount;


    public SearchResponse(List<Trainer> trainersFound, String message, int page, int totalCount) {
        this.trainersFound = trainersFound;
        this.message = message;
        this.page = page;
        this.totalCount = totalCount;
    }

    public SearchResponse() {
    }

    public List<Trainer> getTrainersFound() {
        return trainersFound;
    }

    public void setTrainersFound(List<Trainer> trainersFound) {
        this.trainersFound = trainersFound;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
