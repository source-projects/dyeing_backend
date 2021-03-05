package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.AddMachineInfo.AddBoilerInfo;
import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.UpdateMachineInfo.UpdateBoilerRecord;
import com.main.glory.model.machine.request.BoilerRecordBasedOnFilter;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.response.BoilerFilter;
import com.main.glory.servicesImpl.BoilerRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoilerController extends ControllerConfig {

    @Autowired
    BoilerRecordImpl boilerRecordService;

    @PostMapping(value="/machine/boilerRecord")
    public ResponseEntity<GeneralResponse<Boolean>> saveBoilerRecord(@RequestBody AddBoilerInfo boilerMachineRecord) throws Exception {

        GeneralResponse<Boolean> result;
        if(boilerMachineRecord==null)
        {
            result = new GeneralResponse<Boolean>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            boilerRecordService.saveMachine(boilerMachineRecord);
            result = new GeneralResponse<Boolean>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get boiler record as per the requirement
    @PostMapping(value="/machine/boilerRecord/basedOnFilter/")
    public ResponseEntity<GeneralResponse<List<BoilerMachineRecord>>> getBoilerRecordByFilter(@RequestBody BoilerRecordBasedOnFilter filter) throws Exception {
        GeneralResponse<List<BoilerMachineRecord>> result;
        if(filter==null)
        {
            result = new GeneralResponse<>(null, "machine info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            List<BoilerMachineRecord> list = boilerRecordService.getBoilerRecordBasedOnFilter(filter);
            if(list!=null)
            result = new GeneralResponse<>(list, "Machine fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(null, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/machine/boilerRecord/allMachineRecord")
    public ResponseEntity<GeneralResponse<List<BoilerMachineRecord>>> getAllMachine() throws Exception {

        GeneralResponse<List<BoilerMachineRecord>> result;
        boolean flag;
        try {

            List<BoilerMachineRecord> machineMasts = boilerRecordService.getAllMachineRecord();
            if(machineMasts.isEmpty())
            {
                result = new GeneralResponse<>(null, "Machine Data not found ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            result = new GeneralResponse<>(machineMasts, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/machine/boilerRecord/filter/")
    public ResponseEntity<GeneralResponse<List<BoilerFilter>>> getDataBasedOnFilter(@RequestBody GetRecordBasedOnFilter record) throws Exception {

        GeneralResponse<List<BoilerFilter>> result;
        boolean flag;
        try {

            List<BoilerFilter> machineRecord = boilerRecordService.getDataBasedOnFilter(record);
            if(machineRecord.isEmpty())
            {
                result = new GeneralResponse<>(null, "Machine Data not found ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            result = new GeneralResponse<>(machineRecord, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping(value="/machine/boilerRecord/update/")
    public ResponseEntity<GeneralResponse<Boolean>> updateBoilerRecord(@RequestBody UpdateBoilerRecord record) throws Exception {

        GeneralResponse<Boolean> result ;
        boolean flag;
        try {

            flag = boilerRecordService.updateRecord(record);
            if(flag==true)
            {
                result = new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            result = new GeneralResponse<>(false, "Data not updated", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



}
