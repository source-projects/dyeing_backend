package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.Constant;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.purchase.Purchase;
import com.main.glory.model.purchase.response.PurchaseResponse;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.ProductionPlanImpl;
import com.main.glory.servicesImpl.PurchaseImpl;
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
public class PurchaseController extends ControllerConfig {

    @Autowired
    PurchaseImpl purchaseService;

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;


    @PostMapping("/purchase")
    public ResponseEntity<GeneralResponse<Boolean,Object>> addPurchase(@RequestBody Purchase record){
        GeneralResponse<Boolean,Object> result;
        try{

            if(record==null)
                throw new Exception(Constant.Null_Record_Passed);

            purchaseService.addPurchase(record);
            result= new GeneralResponse<>(true, Constant.Purchase_Added, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/purchase")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updatePurchase(@RequestBody Purchase record,@RequestHeader Map<String, String> headers){
        GeneralResponse<Boolean,Object> result;
        try{

            if(record==null)
                throw new Exception(Constant.Null_Record_Passed);

            purchaseService.updatePurchase(record,headers.get("id"));
            result= new GeneralResponse<>(true, Constant.Purchase_Updated, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/purchase/get")
    public ResponseEntity<GeneralResponse<List<PurchaseResponse>,Object>> getAllPurchase(){
        GeneralResponse<List<PurchaseResponse>,Object> result;
        try{

            List<PurchaseResponse> list = purchaseService.getAllPurchaseRecord();
            if(list.isEmpty())
            {
                result= new GeneralResponse<>(list, Constant.Purchase_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
            result= new GeneralResponse<>(list, Constant.Purchase_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/purchase/delete/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deletePurchaseById(@PathVariable(name = "id")Long id){
        GeneralResponse<Boolean,Object> result;
        try{

            purchaseService.deleteRecordById(id);
            result= new GeneralResponse<>(true, Constant.Purchase_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/purchase/get/{id}")
    public ResponseEntity<GeneralResponse<PurchaseResponse,Object>> getPurchaseById(@PathVariable(name = "id") Long id){
        GeneralResponse<PurchaseResponse,Object> result;
        try{

            if(id==null)
                throw new Exception(Constant.Null_Record_Passed);

            PurchaseResponse list = purchaseService.getPurchaseRecordById(id);
            if(list==null)
            {
                result= new GeneralResponse<>(list, Constant.Purchase_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            }
            else
                result= new GeneralResponse<>(list, Constant.Purchase_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/purchase/update/{id}/{flag}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updatePurchaseStatus(@PathVariable(name = "id") Long id,@PathVariable(name = "flag") Boolean flag,@RequestHeader Map<String, String> headers){
        GeneralResponse<Boolean,Object> result;
        try{

            if(id==null||flag==null)
                throw new Exception(Constant.Null_Record_Passed);

            purchaseService.updatePurchaseRecordWithAdmin(id,flag,headers.get("id"));
            result= new GeneralResponse<>(true, Constant.Purchase_Updated, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/purchase/status")
    public ResponseEntity<GeneralResponse<List<PurchaseResponse>,Object>> getPurchaseListBasedOnStatus(@RequestParam(name = "flag") Boolean flag,@RequestHeader Map<String, String> headers){
        GeneralResponse<List<PurchaseResponse>,Object> result;
        try{


            List<PurchaseResponse> list = purchaseService.getAllPurchaseRecordBasedOnFlag(flag,headers.get("id"));
            if (list.isEmpty())
            {
                result = new GeneralResponse<>(list, Constant.Purchase_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else {
                result = new GeneralResponse<>(list, Constant.Purchase_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



}
