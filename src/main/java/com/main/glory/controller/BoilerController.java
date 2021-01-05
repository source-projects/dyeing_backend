package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.AddMachineInfo.AddBoilerInfo;
import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.request.BoilerRecordBasedOnFilter;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.response.BoilerFilter;
import com.main.glory.servicesImpl.BoilerRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoilerController extends ControllerConfig {

    @Autowired
    BoilerRecordImpl boilerRecordService;


    @PostMapping(value="/boilerRecord")
    public GeneralResponse<Boolean> saveBoilerRecord(@RequestBody AddBoilerInfo boilerMachineRecord) throws Exception {
        if(boilerMachineRecord==null)
        {
            return new GeneralResponse<Boolean>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            boilerRecordService.saveMachine(boilerMachineRecord);
            return new GeneralResponse<Boolean>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get boiler record as per the requirement
    @PostMapping(value="/boilerRecord/basedOnFilter/")
    public GeneralResponse<List<BoilerMachineRecord>> getBoilerRecordByFilter(@RequestBody BoilerRecordBasedOnFilter filter) throws Exception {
        if(filter==null)
        {
            return new GeneralResponse<>(null, "machine info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            List<BoilerMachineRecord> list = boilerRecordService.getBoilerRecordBasedOnFilter(filter);
            if(list!=null)
            return new GeneralResponse<>(list, "Machine fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(null, "data not found", false
                        , System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="/boiler/allMachineRecord")
    public GeneralResponse<List<BoilerMachineRecord>> getAllMachine() throws Exception {

        boolean flag;
        try {

            List<BoilerMachineRecord> machineMasts = boilerRecordService.getAllMachineRecord();
            if(machineMasts.isEmpty())
            {
                return new GeneralResponse<>(null, "Machine Data not found ", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            return new GeneralResponse<>(machineMasts, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/boiler/filter/")
    public GeneralResponse<List<BoilerFilter>> getDataBasedOnFilter(@RequestBody GetRecordBasedOnFilter record) throws Exception {

        boolean flag;
        try {

            List<BoilerFilter> machineRecord = boilerRecordService.getDataBasedOnFilter(record);
            if(machineRecord.isEmpty())
            {
                return new GeneralResponse<>(null, "Machine Data not found ", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            return new GeneralResponse<>(machineRecord, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
