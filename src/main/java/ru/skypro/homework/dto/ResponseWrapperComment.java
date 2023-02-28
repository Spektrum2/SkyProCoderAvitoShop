package ru.skypro.homework.dto;

public class ResponseWrapperComment {
    private Integer count;
    private CommentRecord results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CommentRecord getResults() {
        return results;
    }

    public void setResults(CommentRecord results) {
        this.results = results;
    }
}
