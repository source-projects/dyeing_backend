package com.main.glory.model.machine.request;
import java.util.List;

import com.main.glory.filters.Filter;

import org.springframework.data.domain.Sort;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaginatedData  {
    private List<Filter> parameters;
    private String sortBy;
    private boolean isAnd;
    private String sortOrder;
    private int pageIndex;
    private int pageSize;
    public PaginatedData(List<Filter> parameters, String sortBy, int pageIndex, int pageSize,String sortOrder) {
        this.parameters = parameters;
        this.sortBy = sortBy;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sortOrder=sortOrder;
    }
}
