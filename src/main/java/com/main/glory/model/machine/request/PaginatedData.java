package com.main.glory.model.machine.request;
import java.util.List;

import com.main.glory.filters.Filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedData  {
    private List<Filter> parameters;
    private String sortBy;
    private String sortOrder;
    private int pageIndex;
    private int pageSize;
    public boolean isAnd;

    
    public PaginatedData(List<Filter> parameters, String sortBy, boolean isAnd, String sortOrder, int pageIndex,
            int pageSize) {
        this.parameters = parameters;
        this.sortBy = sortBy;
        this.isAnd = isAnd;
        this.sortOrder = sortOrder;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public PaginatedData(PaginatedData data) {
        this.parameters = data.getParameters();
        this.sortBy = data.getSortBy();
        this.isAnd=data.isAnd;
        this.pageIndex = data.getPageIndex();
        this.pageSize = data.getPageSize();
        this.sortOrder=data.getSortBy();
    }

}
