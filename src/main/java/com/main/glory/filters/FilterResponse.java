package com.main.glory.filters;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FilterResponse <T>{

    List<T> data;
    int pageIndex;
    int pageSize;
    int total;
    public FilterResponse(List<T> data, int pageIndex, int pageSize, int total) {
        this.data = data;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
    }

}
