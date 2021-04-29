package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.Constant;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetStockBasedOnFilter;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithoutBatches;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.DispatchMastImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    Constant constant;



    //get batches without production plan
    @GetMapping("/stockBatch/batch/withoutProductionPlan/all")
    public ResponseEntity<GeneralResponse<List<GetBatchWithControlId>,Object>> getAllBatchWithoutProductionPlan(){
        GeneralResponse<List<GetBatchWithControlId>,Object> result;
        try{

            List<GetBatchWithControlId> stockMast = stockBatchService.getAllBatchWithoutProductionPlan();
            if(!stockMast.isEmpty()){
                result = new GeneralResponse<>(stockMast, constant.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }else{
                result = new GeneralResponse<>(null, constant.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);

        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get stock without batches
    @GetMapping("/stockBatch/batch/stockWithoutBatches/all")
    public ResponseEntity<GeneralResponse<List<GetAllStockWithoutBatches>,Object>> stockWithoutBatches(){
        GeneralResponse<List<GetAllStockWithoutBatches>,Object> result;
        try{

            List<GetAllStockWithoutBatches> stockMast = stockBatchService.getStockListWithoutBatches();
            if(!stockMast.isEmpty()){
                result = new GeneralResponse<>(stockMast, constant.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }else{
                result = new GeneralResponse<>(null, constant.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);

        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    //get stock based on filter
    @PostMapping("/stockBatch/getStockBasedOnFilter")
    public ResponseEntity<GeneralResponse<List<StockMast>,Object>> getStockBasedOnFilter(@RequestBody GetStockBasedOnFilter filter){
        GeneralResponse<List<StockMast>,Object> result;
        try{

            List<StockMast> stockFiltersData = stockBatchService.getStockBasedOnFilter(filter);
            if(!stockFiltersData.isEmpty())
                result = new GeneralResponse<>(stockFiltersData, constant.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result = new GeneralResponse<>(null, constant.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,filter);

            logService.saveLog(result,request,debugAll);

        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,filter);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    //get invoice number based on filter
    /*@PostMapping("/dispatch/getInvoiceListBasedOnFilter")
    public GeneralResponse<List<InvoiceWithBatch>> getInvoiceListBasedOnFilter(@RequestBody GetInvoiceBasedOnFilter filter){
        try{

            List<InvoiceWithBatch> filterData = dispatchMastService.getInvoiceListBasedOnFilter(filter);
            if(!filterData.isEmpty())
                result = new GeneralResponse<>(filterData, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.OK);

        }catch(Exception e){
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }

    }*/



}
