package com.main.glory.controller;

import com.main.glory.model.GeneralResponse;
import com.main.glory.model.machine.AddMachineInfo.AddMachineInfo;
import com.main.glory.model.machine.AddMachineInfo.AddMachineRecord;
import com.main.glory.model.machine.MachineCategory;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.machine.category.AddCategory;
import com.main.glory.model.machine.response.GetAllCategory;
import com.main.glory.model.machine.response.GetAllMachine;
import com.main.glory.model.machine.response.GetMachineByIdWithFilter;
import com.main.glory.model.program.request.AddProgramWithProgramRecord;
import com.main.glory.servicesImpl.MachineServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import com.main.glory.config.ControllerConfig;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MachineController extends ControllerConfig {


    @Autowired
    private MachineServiceImpl machineService;

    @PostMapping(value="/machine")
    public GeneralResponse<Boolean> saveMachine(@RequestBody AddMachineInfo machine) throws Exception {
        if(machine==null)
        {
            return new GeneralResponse<Boolean>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            machineService.saveMachine(machine);
            return new GeneralResponse<Boolean>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/machine/addRecord/{name}/{datetime}")
    public GeneralResponse<Boolean> saveMachineRecord(@PathVariable(name="datetime") long date, @PathVariable(name="name") String name) throws Exception {
        boolean flag;
        try {

            machineService.addTempMachineRecord(name,date);
            return new GeneralResponse<Boolean>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/machine/addCategory")
    public GeneralResponse<Boolean> saveMachineCategory(@RequestBody AddCategory machine) throws Exception {
        if(machine==null)
        {
            return new GeneralResponse<Boolean>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            machineService.saveMachineCategory(machine);
            return new GeneralResponse<Boolean>(null, "Machine Category added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/machine")
    public GeneralResponse<Boolean> saveMachineRecord(@RequestParam(name="name") String name,@RequestParam(name="speed") Double speed) throws Exception {
        if(name==null && speed == null)
        {
            return new GeneralResponse<Boolean>(false, "info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            machineService.saveMachineRecord(name,speed);
            return new GeneralResponse<Boolean>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="/machine/allCategory")
    public GeneralResponse<List<GetAllCategory>> getAllMachineCategory() throws Exception {

        boolean flag;
        try {

            var x = machineService.getAllCategory();
            return new GeneralResponse<>(x, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/machine/getMachine/ByCategory")
    public GeneralResponse<List<GetAllMachine>> getAllMachineByCategory(@RequestParam(name="id") Long id) throws Exception {

        boolean flag;
        try {

            var x = machineService.getAllMachineByCategory(id);
            return new GeneralResponse<>(x, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/machine/all")
    public GeneralResponse<List<GetAllMachine>> getAllMachine() throws Exception {

        boolean flag;
        try {

            List<GetAllMachine> machineMasts = machineService.getAllMachine();
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

    @GetMapping(value="/machine/{id}")
    public GeneralResponse<GetAllMachine> getMachineById(@PathVariable(name="id") Long id) throws Exception {

        boolean flag;
        try {

            if(id==null)
            {
                return new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            GetAllMachine machineMasts = machineService.getMachineById(id);
            if(machineMasts ==null)
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

    @PostMapping(value="/machineWithFilter")
    public GeneralResponse<GetAllMachine> getMachineByIdWithFilter(@RequestBody GetMachineByIdWithFilter getMachine) throws Exception {

        boolean flag;
        try {

            if(getMachine==null)
            {
                return new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            GetAllMachine machineMasts = machineService.getMachineByIdWithFilter(getMachine);
            if(machineMasts ==null)
            {
                return new GeneralResponse<>(null, "Machine Data not found ", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            return new GeneralResponse<>(machineMasts, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/machine/")
    public GeneralResponse<Boolean> deleteMachineById(@PathVariable(name="id") Long id) throws Exception {

        boolean flag;
        try {

            if(id==null)
            {
                return new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            flag = machineService.deleteMachineById(id);
            if(flag ==true)
            return new GeneralResponse<>(true, "Machine Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

        }
        return null;
    }




}
