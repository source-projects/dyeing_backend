package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.AddMachineInfo.AddThermopackInfo;
import com.main.glory.model.machine.Thermopack;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.request.ThermopackRecordBasedOnShift;
import com.main.glory.model.machine.response.ThermopackFilterRecord;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.ThermopackImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ThermopackController extends ControllerConfig {

    @Autowired
    ThermopackImpl thermopackService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @PostMapping(value="/machine/thermopack/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveThermopackRecord(@RequestBody List<AddThermopackInfo> thermopackRecord) throws Exception {
        GeneralResponse<Boolean,Object> result;
        if(thermopackRecord==null)
        {
            //result =  new GeneralResponse<Boolean>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.OK);
            throw new Exception(ConstantFile.Null_Record_Passed);
        }

        boolean flag;
        try {

            thermopackService.saveThermopackRecord(thermopackRecord);
            result =new GeneralResponse<>(null, ConstantFile.Machine_Data_Added, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/machine/thermoPack/allMachineRecord")
    public ResponseEntity<GeneralResponse<List<Thermopack>,Object>> getAllMachine() throws Exception {

        GeneralResponse<List<Thermopack>,Object> result;
        boolean flag;
        try {

            List<Thermopack> machineMasts = thermopackService.getAllMachineRecord();
            if(machineMasts.isEmpty())
            {
                result = new GeneralResponse<>(null, ConstantFile.Machine_Data_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString()+"?"+request.getQueryString());
            }
            else
            result = new GeneralResponse<>(machineMasts, ConstantFile.Machine_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/machine/thermopack/filter/")
    public ResponseEntity<GeneralResponse<List<ThermopackFilterRecord>,Object>> getDataBasedOnFilter(@RequestBody GetRecordBasedOnFilter record) throws Exception {

        GeneralResponse<List<ThermopackFilterRecord>,Object> result;
        boolean flag;
        try {

            List<ThermopackFilterRecord> machineRecord = thermopackService.getDataBasedOnFilter(record);
            if(machineRecord.isEmpty())
            {
                result = new GeneralResponse<>(null, ConstantFile.Machine_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,record);
            }
            else {
                result = new GeneralResponse<>(machineRecord, ConstantFile.Machine_Data_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);
            }
            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


//record based on shift
    @PostMapping(value="/machine/thermopack/filter/basedOnShift/")
    public ResponseEntity<GeneralResponse<List<Thermopack>,Object>> getDataBasedOnFilter(@RequestBody ThermopackRecordBasedOnShift record) throws Exception {

        GeneralResponse<List<Thermopack>,Object> result;
        boolean flag;
        try {

            List<Thermopack> machineRecord = thermopackService.getRecordBasedOnShift(record);
            if(machineRecord.isEmpty())
            {
                result = new GeneralResponse<>(null, ConstantFile.Machine_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,record);
            }
            else
            result = new GeneralResponse<>(machineRecord, ConstantFile.Machine_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
