package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.AddMachineInfo.AddBoilerInfo;
import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.UpdateMachineInfo.UpdateBoilerRecord;
import com.main.glory.model.machine.request.BoilerRecordBasedOnFilter;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.response.BoilerFilter;
import com.main.glory.servicesImpl.BoilerRecordImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BoilerController extends ControllerConfig {

    @Autowired
    BoilerRecordImpl boilerRecordService;

    @Autowired
    LogServiceImpl logService;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @Autowired
    HttpServletRequest request;

    ConstantFile constantFile;

    @PostMapping(value="/machine/boilerRecord")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveBoilerRecord(@RequestBody AddBoilerInfo boilerMachineRecord) throws Exception {

        GeneralResponse<Boolean,Object> result;
        if(boilerMachineRecord==null)
        {
            result = new GeneralResponse<>(false, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
        }

        boolean flag;
        try {

            boilerRecordService.saveMachine(boilerMachineRecord);
            result = new GeneralResponse<>(null, constantFile.Machine_Data_Added, true, System.currentTimeMillis(), HttpStatus.OK,boilerMachineRecord);
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,boilerMachineRecord);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get boiler record as per the requirement
    @PostMapping(value="/machine/boilerRecord/basedOnFilter/")
    public ResponseEntity<GeneralResponse<List<BoilerMachineRecord>,Object>> getBoilerRecordByFilter(@RequestBody BoilerRecordBasedOnFilter filter) throws Exception {
        GeneralResponse<List<BoilerMachineRecord>,Object> result;
        if(filter==null)
        {
            result = new GeneralResponse<>(null, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }

        boolean flag;
        try {

            List<BoilerMachineRecord> list = boilerRecordService.getBoilerRecordBasedOnFilter(filter);
            if(list!=null)
            result = new GeneralResponse<>(list, constantFile.Machine_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result = new GeneralResponse<>(null, constantFile.Machine_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,filter);
            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,filter);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/machine/boilerRecord/allMachineRecord")
    public ResponseEntity<GeneralResponse<List<BoilerMachineRecord>,Object>> getAllMachine() throws Exception {

        GeneralResponse<List<BoilerMachineRecord>,Object> result;
        boolean flag;
        try {

            List<BoilerMachineRecord> machineMasts = boilerRecordService.getAllMachineRecord();
            if(machineMasts.isEmpty())
            {
                result = new GeneralResponse<>(null, constantFile.Machine_Data_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else
            result = new GeneralResponse<>(machineMasts, constantFile.Machine_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/machine/boilerRecord/filter/")
    public ResponseEntity<GeneralResponse<List<BoilerFilter>,Object>> getDataBasedOnFilter(@RequestBody GetRecordBasedOnFilter record) throws Exception {

        GeneralResponse<List<BoilerFilter>,Object> result;
        boolean flag;
        try {

            List<BoilerFilter> machineRecord = boilerRecordService.getDataBasedOnFilter(record);
            if(machineRecord.isEmpty())
            {
                result = new GeneralResponse<>(null, constantFile.Machine_Data_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);
            }
            else
            result = new GeneralResponse<>(machineRecord, constantFile.Machine_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);


        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping(value="/machine/boilerRecord/update/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateBoilerRecord(@RequestBody UpdateBoilerRecord record) throws Exception {

        GeneralResponse<Boolean,Object> result ;
        boolean flag;
        try {

            flag = boilerRecordService.updateRecord(record);
            if(flag==true)
            {
                result = new GeneralResponse<>(true, constantFile.Machine_Data_Updated, true, System.currentTimeMillis(), HttpStatus.OK,record);
            }
            else
            result = new GeneralResponse<>(false, constantFile.Machine_Data_Not_Updated, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



}
