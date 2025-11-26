package com.softwareascraft.practice.dto.response;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsResponse<T> {
    private List<T> results;
    private Long totalResults;
    private Integer page;
    private Integer pageSize;

    public SearchResultsResponse() {
        this.results = new ArrayList<>();
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
