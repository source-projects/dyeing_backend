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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GeneralResponse<Boolean>> updateStockBatch(@RequestBody List<BatchData> batchData) {
        GeneralResponse<Boolean> result;
        try {
            batchImpl.updateFinishMtrBatch(batchData);
            result = new GeneralResponse<>(true, "Job card created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/batch/delete/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteBatch(@PathVariable("id") Long id){
        GeneralResponse<Boolean> result;
        try{
            System.out.println("deleting batch with id:"+id);
            batchImpl.deleteBatch(id);
            result = new GeneralResponse<>(true,"batch deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));


    }

    @GetMapping("/batch/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<List<BatchData>>> getFinishMtrBatchById(@PathVariable(value = "batchId") String batchId,@PathVariable(value = "controlId") String controlId){

        GeneralResponse<List<BatchData>> result;
        try{
            if(batchId!=null){
                List<BatchData> batchData = batchImpl.getBatchById(batchId,controlId);

                if(!batchData.isEmpty())
                result = new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                else
                    result = new GeneralResponse<>(batchData,"no data found",false,System.currentTimeMillis(),HttpStatus.OK);
            }
            else{
                result = new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/batch/jobCardDetails/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<GetCompleteFinishMtrDetail>> getFinishMtrWithAllDataBatchById(@PathVariable(value = "batchId") String batchId, @PathVariable(value = "controlId") Long controlId){
        GeneralResponse<GetCompleteFinishMtrDetail> result;
        try{
            if(batchId!=null){
                GetCompleteFinishMtrDetail batchData = batchImpl.getAllDetailBy(batchId,controlId);

                if(batchData==null)
                    result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

                else
                result = new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            }
            else{
                result = new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch(Exception e){
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }




}
