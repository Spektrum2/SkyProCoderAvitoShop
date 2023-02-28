package ru.skypro.homework.dto;

public class ResponseWrapperAds {
    private Integer count;
    private AdsNotFull results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public AdsNotFull getResults() {
        return results;
    }

    public void setResults(AdsNotFull results) {
        this.results = results;
    }
}
