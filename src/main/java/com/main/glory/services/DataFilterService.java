package com.main.glory.services;

import java.util.List;

import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.filters.StockDataBatchData.StockMastFilter;
import com.main.glory.model.StockDataBatchData.StockMast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DataFilterService {
    @Autowired
    StockMastDao stockMastDao;
    public  List<StockMast> getFilteredStockMast(StockMastFilter filter,int pageSize,int pageNumber) throws Exception{
    Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<StockMast> stockMastPage=stockMastDao.getAllFilteredStockMastPaged(filter.getId(),filter.getStockInType(),filter.getPartyId(),filter.getBillNo(),DataConversion.stringToDate(filter.getBillDate()),DataConversion.stringToDate(filter.getChlDate()),filter.getUnit(),filter.getUpdatedBy(),filter.getCreatedBy(),filter.getUserHeadId(),filter.getIsProductionPlanned(),DataConversion.stringToDate(filter.getCreatedDate()),filter.getReceiveDate(),DataConversion.stringToDate(filter.getUpdatedDate()),filter.getQualityId()   ,pageable);
         
        return stockMastPage.toList();
    }
}
