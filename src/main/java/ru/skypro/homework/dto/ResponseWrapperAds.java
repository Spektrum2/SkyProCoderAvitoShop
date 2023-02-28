package ru.skypro.homework.dto;

public class ResponseWrapperAds {
    private Integer count;
    private Ads results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Ads getResults() {
        return results;
    }

    public void setResults(Ads results) {
        this.results = results;
    }
}
