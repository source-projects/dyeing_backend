package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.Thermopack;
import com.main.glory.model.machine.response.GetAllMachine;
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

    @PostMapping(value="/thermoPackRecord")
    public GeneralResponse<Boolean> saveBoilerRecord(@RequestBody Thermopack thermopackRecord) throws Exception {
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

}
