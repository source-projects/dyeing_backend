package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.CreateMergeBatch;
import com.main.glory.model.StockDataBatchData.response.BatchToPartyAndQuality;
import com.main.glory.model.StockDataBatchData.response.BatchToPartyQualityWithGr;
import com.main.glory.model.StockDataBatchData.response.MergeBatchId;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MergeBatchController extends ControllerConfig {

    @Autowired
    private StockBatchServiceImpl stockBatchService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;


    //create the merge batch list
    @PostMapping("/stockBatch/create/mergeBatchList")
    public ResponseEntity<GeneralResponse<Boolean,Object>> createMergeBatchListBatch(@RequestBody CreateMergeBatch record, @RequestHeader Map<String, String> headers) throws Exception {
        GeneralResponse<Boolean,Object> result = null;
        try {
            stockBatchService.createMergeBatchList(record);
            result = new GeneralResponse<>(true, CommonMessage.MergeBatch_Added, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/stockBatch/update/mergeBatchList")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateMergeBatchListBatch(@RequestBody CreateMergeBatch record, @RequestHeader Map<String, String> headers) throws Exception {
        GeneralResponse<Boolean,Object> result = null;
        try {
            stockBatchService.updateMergeBatchList(record);
            result = new GeneralResponse<>(true, CommonMessage.MergeBatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/stockBatch/get/mergeBatchList")
    public ResponseEntity<GeneralResponse<List<BatchToPartyAndQuality>,Object>> getAllMergeBatchId() throws Exception {
        GeneralResponse<List<BatchToPartyAndQuality>,Object> result = null;
        try {
            List<BatchToPartyAndQuality> list = stockBatchService.getAllMergeBatchId();

            if(!list.isEmpty())
                result = new GeneralResponse<>(list, CommonMessage.MergeBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(list, CommonMessage.MergeBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/stockBatch/get/mergeBatchListBy")
    public ResponseEntity<GeneralResponse<BatchToPartyQualityWithGr,Object>> getMergeBatchByMergeBatchId(@RequestParam(name = "mergeBatchId")String mergeBatchId) throws Exception {
        GeneralResponse<BatchToPartyQualityWithGr,Object> result = null;
        try {
            BatchToPartyQualityWithGr list = stockBatchService.getMergeBatchByMergeBatchId(mergeBatchId);

            if(list!=null)
                result = new GeneralResponse<>(list, CommonMessage.MergeBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(list, CommonMessage.MergeBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/stockBatch/delete/mergeBatchListBy")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteMergeBatchByMergeBatchId(@RequestParam(name = "mergeBatchId")String mergeBatchId) throws Exception {
        GeneralResponse<Boolean,Object> result = null;
        try {
            if(mergeBatchId==null)
                throw new Exception(CommonMessage.Null_Record_Passed);

            stockBatchService.deleteMergeBatchByMergeBatchId(mergeBatchId);
            result = new GeneralResponse<>(true, CommonMessage.MergeBatch_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
