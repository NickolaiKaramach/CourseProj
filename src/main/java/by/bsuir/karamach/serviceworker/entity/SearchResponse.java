package by.bsuir.karamach.serviceworker.entity;

import java.util.List;

public class SearchResponse {
    private List<Trainer> trainersFound;
    private String message;
    private int pageNum;

    public SearchResponse(List<Trainer> trainersFound, String message, int pageNum) {
        this.trainersFound = trainersFound;
        this.message = message;
        this.pageNum = pageNum;
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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
