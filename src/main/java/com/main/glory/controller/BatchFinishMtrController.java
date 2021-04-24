package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetCompleteFinishMtrDetail;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BatchFinishMtrController extends ControllerConfig {

    @Autowired
    LogServiceImpl logService;

    CommonMessage commonMessage;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @Autowired
    HttpServletRequest request;

    @Autowired
    BatchImpl batchImpl;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @PutMapping("/batch/finishMtr")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateStockBatch(@RequestBody List<BatchData> batchData) {
        GeneralResponse<Boolean,Object> result;
        try {
            batchImpl.updateFinishMtrBatch(batchData);

            result = new GeneralResponse<>(true, commonMessage.FinishMtr_Data_Added, true, System.currentTimeMillis(), HttpStatus.OK,batchData);

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,batchData);
            logService.saveLog(result,request,true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/batch/delete/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteBatch(@PathVariable("id") Long id){
        GeneralResponse<Boolean,Object> result;
        try{
            //System.out.println("deleting batch with id:"+id);
            batchImpl.deleteBatch(id);
            result = new GeneralResponse<>(true,commonMessage.Batch_Data_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));


    }

    @GetMapping("/batch/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<List<BatchData>,Object>> getFinishMtrBatchById(@PathVariable(value = "batchId") String batchId,@PathVariable(value = "controlId") String controlId){

        GeneralResponse<List<BatchData>, Object> result;
        try{
            if(batchId!=null){
                List<BatchData> batchData = batchImpl.getBatchById(batchId,controlId);

                if(!batchData.isEmpty())
                result = new GeneralResponse<>(batchData, commonMessage.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                else
                    result = new GeneralResponse<>(batchData,commonMessage.StockBatch_Not_Found,false,System.currentTimeMillis(),HttpStatus.OK,request.getRequestURI());

                logService.saveLog(result,request,debugAll);
            }
            else{
                result = new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                logService.saveLog(result,request,true);
            }
        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/batch/jobCardDetails/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<GetCompleteFinishMtrDetail,Object>> getFinishMtrWithAllDataBatchById(@PathVariable(value = "batchId") String batchId, @PathVariable(value = "controlId") Long controlId){
        GeneralResponse<GetCompleteFinishMtrDetail,Object> result;
        try{
            if(batchId!=null){
                GetCompleteFinishMtrDetail batchData = batchImpl.getAllDetailBy(batchId,controlId);

                if(batchData==null)
                    result = new GeneralResponse<>(null, commonMessage.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

                else
                result = new GeneralResponse<>(batchData, commonMessage.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

                logService.saveLog(result,request,debugAll);
            }
            else{
                result = new GeneralResponse<>(null, commonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                logService.saveLog(result,request,true);
            }
        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }




}
