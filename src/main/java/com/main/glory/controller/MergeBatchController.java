package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.CreateMergeBatch;
import com.main.glory.model.StockDataBatchData.response.MergeBatchId;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public class MergeBatchController extends ControllerConfig {

    @Autowired
    private StockBatchServiceImpl stockBatchService;

    //create the merge batch list
    @PostMapping("/stockBatch/create/mergeBatchList")
    public ResponseEntity<GeneralResponse<Boolean>> createMergeBatchListBatch(@RequestBody CreateMergeBatch record, @RequestHeader Map<String, String> headers) throws Exception {
        GeneralResponse<Boolean> result = null;
        try {
            stockBatchService.createMergeBatchList(record);
            result = new GeneralResponse<>(true, "Merge batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/stockBatch/update/mergeBatchList")
    public ResponseEntity<GeneralResponse<Boolean>> updateMergeBatchListBatch(@RequestBody CreateMergeBatch record, @RequestHeader Map<String, String> headers) throws Exception {
        GeneralResponse<Boolean> result = null;
        try {
            stockBatchService.updateMergeBatchList(record);
            result = new GeneralResponse<>(true, "Merge batch updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/stockBatch/get/mergeBatchList")
    public ResponseEntity<GeneralResponse<List<MergeBatchId>>> getAllMergeBatchId() throws Exception {
        GeneralResponse<List<MergeBatchId>> result = null;
        try {
            List<MergeBatchId> list = stockBatchService.getAllMergeBatchId();

            if(!list.isEmpty())
                result = new GeneralResponse<>(list, "Merge batch fetch successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(list, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/stockBatch/get/mergeBatchListBy")
    public ResponseEntity<GeneralResponse<CreateMergeBatch>> getMergeBatchByMergeBatchId(@RequestParam(name = "mergeBatchId")String mergeBatchId) throws Exception {
        GeneralResponse<CreateMergeBatch> result = null;
        try {
            CreateMergeBatch list = stockBatchService.getMergeBatchByMergeBatchId(mergeBatchId);

            if(list!=null)
                result = new GeneralResponse<>(list, "Merge batch fetch successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(list, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/stockBatch/delete/mergeBatchListBy")
    public ResponseEntity<GeneralResponse<Boolean>> deleteMergeBatchByMergeBatchId(@RequestParam(name = "mergeBatchId")String mergeBatchId) throws Exception {
        GeneralResponse<Boolean> result = null;
        try {
            if(mergeBatchId==null)
                throw new Exception("null id passed");

            stockBatchService.deleteMergeBatchByMergeBatchId(mergeBatchId);
            result = new GeneralResponse<>(true, "Merge batch deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
