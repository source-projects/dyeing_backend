package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.AddMachineInfo.AddThermopackInfo;
import com.main.glory.model.machine.Thermopack;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.request.ThermopackRecordBasedOnShift;
import com.main.glory.model.machine.response.BoilerFilter;
import com.main.glory.model.machine.response.ThermopackFilterRecord;
import com.main.glory.servicesImpl.ThermopackImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ThermopackController extends ControllerConfig {

    @Autowired
    ThermopackImpl thermopackService;

    @PostMapping(value="/thermopack/")
    public GeneralResponse<Boolean> saveThermopackRecord(@RequestBody List<AddThermopackInfo> thermopackRecord) throws Exception {
        if(thermopackRecord==null)
        {
            return new GeneralResponse<Boolean>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            thermopackService.saveThermopackRecord(thermopackRecord);
            return new GeneralResponse<Boolean>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/thermoPack/allMachineRecord")
    public GeneralResponse<List<Thermopack>> getAllMachine() throws Exception {

        boolean flag;
        try {

            List<Thermopack> machineMasts = thermopackService.getAllMachineRecord();
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

    @PostMapping(value="/thermopack/filter/")
    public GeneralResponse<List<ThermopackFilterRecord>> getDataBasedOnFilter(@RequestBody GetRecordBasedOnFilter record) throws Exception {

        boolean flag;
        try {

            List<ThermopackFilterRecord> machineRecord = thermopackService.getDataBasedOnFilter(record);
            if(machineRecord.isEmpty())
            {
                return new GeneralResponse<>(null, "Machine Data not found ", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            return new GeneralResponse<>(machineRecord, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


//record based on shift
    @PostMapping(value="/thermopack/filter/basedOnShift/")
    public GeneralResponse<List<Thermopack>> getDataBasedOnFilter(@RequestBody ThermopackRecordBasedOnShift record) throws Exception {

        boolean flag;
        try {

            List<Thermopack> machineRecord = thermopackService.getRecordBasedOnShift(record);
            if(machineRecord.isEmpty())
            {
                return new GeneralResponse<>(null, "Machine Data not found ", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            return new GeneralResponse<>(machineRecord, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
}
