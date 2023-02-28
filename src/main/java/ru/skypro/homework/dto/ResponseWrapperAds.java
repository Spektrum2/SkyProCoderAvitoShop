package ru.skypro.homework.dto;

import java.util.List;

public class ResponseWrapperAds {
    private Integer count;
    private List<AdsNotFull> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<AdsNotFull> getResults() {
        return results;
    }

    public void setResults(List<AdsNotFull> results) {
        this.results = results;
    }
}
