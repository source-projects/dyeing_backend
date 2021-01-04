package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetStockBasedOnFilter;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithoutBatches;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.dispatch.request.GetInvoiceBasedOnFilter;
import com.main.glory.model.dispatch.request.InvoiceWithBatch;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.DispatchMastImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FilterController extends ControllerConfig {

    @Autowired
    private BatchImpl batchService;

    @Autowired
    DispatchMastImpl dispatchMastService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;


    //get batches without production plan
    @GetMapping("/stockBatch/batch/withoutProductionPlan/all")
    public GeneralResponse<List<GetBatchWithControlId>> getAllBatchWithoutProductionPlan(){
        try{

            List<GetBatchWithControlId> stockMast = stockBatchService.getAllBatchWithoutProductionPlan();
            if(!stockMast.isEmpty()){
                return new GeneralResponse<>(stockMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }else{
                return new GeneralResponse<>(null, "no data found ", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }

        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //get stock without batches
    @GetMapping("/stockBatch/batch/stockWithoutBatches/all")
    public GeneralResponse<List<GetAllStockWithoutBatches>> stockWithoutBatches(){
        try{

            List<GetAllStockWithoutBatches> stockMast = stockBatchService.getStockListWithoutBatches();
            if(!stockMast.isEmpty()){
                return new GeneralResponse<>(stockMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }else{
                return new GeneralResponse<>(null, "no data found ", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }

        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //get stock based on filter
    @PostMapping("/stockBatch/getStockBasedOnFilter")
    public GeneralResponse<List<StockMast>> getStockBasedOnFilter(@RequestBody GetStockBasedOnFilter filter){
        try{

            List<StockMast> stockFiltersData = stockBatchService.getStockBasedOnFilter(filter);
            if(!stockFiltersData.isEmpty())
                return new GeneralResponse<>(stockFiltersData, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            else
                return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //get invoice number based on filter
    @PostMapping("/dispatch/getInvoiceListBasedOnFilter")
    public GeneralResponse<List<InvoiceWithBatch>> getInvoiceListBasedOnFilter(@RequestBody GetInvoiceBasedOnFilter filter){
        try{

            List<InvoiceWithBatch> filterData = dispatchMastService.getInvoiceListBasedOnFilter(filter);
            if(!filterData.isEmpty())
                return new GeneralResponse<>(filterData, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            else
                return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }



}
