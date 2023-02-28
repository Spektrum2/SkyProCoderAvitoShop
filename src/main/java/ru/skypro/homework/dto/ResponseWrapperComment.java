package ru.skypro.homework.dto;

import java.util.List;

public class ResponseWrapperComment {
    private Integer count;
    private List<CommentRecord> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CommentRecord> getResults() {
        return results;
    }

    public void setResults(List<CommentRecord> results) {
        this.results = results;
    }
}
