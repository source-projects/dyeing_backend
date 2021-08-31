package com.main.glory.model.StockDataBatchData.request;

import com.main.glory.filters.Filter;
import com.main.glory.model.machine.request.PaginatedData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllStockBatchPaginatedAndFiltered  {
    String getBy;
    PaginatedData data;
    public GetAllStockBatchPaginatedAndFiltered(String getBy, PaginatedData data) {
        this.getBy = getBy;
        this.data = data;
    }

}
