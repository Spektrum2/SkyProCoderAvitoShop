package ru.skypro.homework.dto;

public class ResponseWrapperComment {
    private Integer count;
    private Comment results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Comment getResults() {
        return results;
    }

    public void setResults(Comment results) {
        this.results = results;
    }
}
