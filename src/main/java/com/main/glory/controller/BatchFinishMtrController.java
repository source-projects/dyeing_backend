package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetCompleteFinishMtrDetail;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BatchFinishMtrController extends ControllerConfig {

    @Autowired
    BatchImpl batchImpl;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @PutMapping("/batch/finishMtr")
    public GeneralResponse<Boolean> updateStockBatch(@RequestBody List<BatchData> batchData) {
        try {
            batchImpl.updateFinishMtrBatch(batchData);
            return new GeneralResponse<>(true, "Job card created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/batch/{controlId}/{batchId}")
    public GeneralResponse<List<BatchData>> getFinishMtrBatchById(@PathVariable(value = "batchId") String batchId,@PathVariable(value = "controlId") Long controlId){
        try{
            if(batchId!=null){
                List<BatchData> batchData = batchImpl.getBatchById(batchId,controlId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/batch/allDetails/{controlId}/{batchId}")
    public GeneralResponse<GetCompleteFinishMtrDetail> getFinishMtrWithAllDataBatchById(@PathVariable(value = "batchId") String batchId, @PathVariable(value = "controlId") Long controlId){
        try{
            if(batchId!=null){
                GetCompleteFinishMtrDetail batchData = batchImpl.getAllDetailBy(batchId,controlId);

                if(batchData==null)
                    return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
   /* @GetMapping("/batch/doc/{controlId}/{batchId}")
    public GeneralResponse<List<BatchData>> getFinishMtrDocBatchById(@PathVariable(value = "batchId") String batchId,@PathVariable(value = "controlId") Long controlId){
        try{
            if(batchId!=null){
                List<BatchData> batchData = batchImpl.getBatchByDocId(batchId,controlId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }*/




}
