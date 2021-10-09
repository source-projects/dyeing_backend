package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.request.ChangeStatus;
import com.main.glory.model.jet.request.JetStart;
import com.main.glory.model.jet.request.UpdateJetData;
import com.main.glory.model.jet.responce.*;
import com.main.glory.servicesImpl.JetServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
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
public class JetController extends ControllerConfig {

    @Autowired
    JetServiceImpl jetService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll;

    ConstantFile constantFile;

    @PostMapping(value="/jet/addJetData")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveJetData(@RequestBody List<AddJetData> jetData) throws Exception {

        GeneralResponse<Boolean,Object> result;
        if(jetData==null)
        {
            //result = new GeneralResponse<>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
            throw new Exception(constantFile.Null_Record_Passed);
        }

        boolean flag;
        try {

            jetService.saveJetData(jetData);
            result = new GeneralResponse<>(null, constantFile.Jet_Added, true, System.currentTimeMillis(), HttpStatus.OK,jetData);

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,jetData);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/jet/updateJetData")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateJetData(@RequestBody UpdateJetData jetDataToUpdate) throws Exception {

        GeneralResponse<Boolean,Object> result;
        if(jetDataToUpdate==null)
        {
            //result = new GeneralResponse<>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
            throw new Exception(constantFile.Null_Record_Passed);
        }

        boolean flag;
        try {

            jetService.updateJetData(jetDataToUpdate);
            result = new GeneralResponse<>(null, constantFile.Jet_Updated, true, System.currentTimeMillis(), HttpStatus.OK,jetDataToUpdate);

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,jetDataToUpdate);
            logService.saveLog(result,request,debugAll);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/jet/updateJetData/productionStatus/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateJetData(@RequestBody ChangeStatus jetDataToUpdate,@RequestHeader Map<String, String> headers) throws Exception {

        GeneralResponse<Boolean,Object> result;
        if(jetDataToUpdate==null)
        {
            throw new Exception(constantFile.Null_Record_Passed);
            //result = new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            jetService.updateProductionStatus(jetDataToUpdate,headers.get("id"));
            result = new GeneralResponse<>(null, constantFile.Jet_Updated, true, System.currentTimeMillis(), HttpStatus.OK,jetDataToUpdate);

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,jetDataToUpdate);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value="/jet/getJetData/{id}")
    public ResponseEntity<GeneralResponse<List<GetJetData>,Object>> getJetData(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<List<GetJetData>,Object> result;
        if(id==null)
        {
            throw new Exception(constantFile.Null_Record_Passed );//result = new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            List<GetJetData> jetDataList = jetService.getJetData(id);
            result = new GeneralResponse<>(jetDataList, constantFile.Jet_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/jet/getAllJetDetail")
    public ResponseEntity<GeneralResponse<List<GetAllJetMast>,Object>> getAllJetData()  throws Exception {

        GeneralResponse<List<GetAllJetMast>,Object> result;
        boolean flag;
        try {

            List<GetAllJetMast> jetMastList = jetService.getAllJetData();
            if(!jetMastList.isEmpty())
            result = new GeneralResponse<>(jetMastList, constantFile.Jet_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
            result = new GeneralResponse<>(jetMastList, constantFile.Jet_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/jet/getAllJetMast")
    public ResponseEntity<GeneralResponse<List<GetAllJetMast>,Object>> getAllJetMast()  throws Exception {

        GeneralResponse<List<GetAllJetMast>,Object> result;
        boolean flag;
        try {

            List<GetAllJetMast> jetMastList = jetService.getAllJetMast();
            if(!jetMastList.isEmpty())
                result = new GeneralResponse<>(jetMastList, constantFile.Jet_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(jetMastList, constantFile.Jet_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get the jet slip
    @GetMapping(value="/jet/getJetSlipData/{jetId}/{productionId}")
    public ResponseEntity<GeneralResponse<GetJetSlip,Object>> getJetSlipData(@PathVariable(name = "jetId") Long jetId , @PathVariable(name = "productionId") Long productionId)  throws Exception {
        GeneralResponse<GetJetSlip,Object> result;
        boolean flag;
        try {
            GetJetSlip slipData = jetService.getJetSlipData(jetId,productionId);
            result = new GeneralResponse<>(slipData, constantFile.Jet_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //remove the production from the jet
    @DeleteMapping(value="/jet/delete/removeProductionFromJet/{jetId}/{productionId}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> removeProductionFromJet(@PathVariable(name = "jetId") Long jetId , @PathVariable(name = "productionId") Long productionId)  throws Exception {
        GeneralResponse<Boolean,Object> result;
        boolean flag;
        try {
            jetService.removeProductionFromJet(jetId,productionId);
            result = new GeneralResponse<>(true, constantFile.Production_Removed, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @GetMapping(value="/jet/getJetDataWithInQueueProdution/byJetId/{id}")
    public ResponseEntity<GeneralResponse<List<GetJetData>,Object>> getJetDataWithInQueueProdution(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<List<GetJetData>,Object> result;
        if(id==null)
        {
             throw new Exception(ConstantFile.Null_Record_Passed);
        }


        boolean flag;
        try {

            List<GetJetData> jetDataList = jetService.getJetDataWithInQueueProdution(id);
            if(!jetDataList.isEmpty())
            result =  new GeneralResponse<>(jetDataList, constantFile.Jet_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result =  new GeneralResponse<>(jetDataList, constantFile.Jet_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/jet/getJet/allStatusList")
    public ResponseEntity<GeneralResponse<List<GetStatus>,Object>> getJetStatusList() throws Exception {

        GeneralResponse<List<GetStatus>,Object> result;
        boolean flag;
        try {

            List<GetStatus> jetDataList = jetService.getJetStatusList();
            result = new GeneralResponse<>(jetDataList, constantFile.Jet_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value="/jet/deleteJetDataByProductionId/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteJetDataByProductionId(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<Boolean,Object> result;
        if(id==null) {
            throw new Exception(constantFile.Null_Record_Passed);//result = new GeneralResponse<>(null, "production info is null", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        try {

            Boolean flag = jetService.deleteJetDataByProductionId(id);
            if(flag==true)
            result = new GeneralResponse<>(true, constantFile.Jet_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(false, constantFile.Production_Unable_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value="/jet/deleteJetMastByJetId/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteJetMastByJetId(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<Boolean, Object> result;
        if(id==null)
        {
            throw new Exception(constantFile.Null_Record_Passed);//result = new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        try {

            Boolean flag = jetService.deleteJetMastByJetId(id);
            if(flag==true)
                result = new GeneralResponse<>(true, constantFile.Jet_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(false, constantFile.Jet_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //save data in hmi mast
    @PostMapping(value="/jet/start")
    public ResponseEntity<GeneralResponse<Boolean,Object>> hmiSaveJetData(@RequestBody JetStart record)  throws Exception {
        GeneralResponse<Boolean,Object> result;
        boolean flag;
        try {
            jetService.hmiSaveJetdata(record);
            result = new GeneralResponse<>(true, constantFile.Jet_Added, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
