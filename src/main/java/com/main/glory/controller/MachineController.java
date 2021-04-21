package com.main.glory.controller;

import com.google.api.Http;
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
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.MachineServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.main.glory.config.ControllerConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MachineController extends ControllerConfig {



    @Autowired
    private MachineServiceImpl machineService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;
    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;


    @PostMapping(value="/machine")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveMachine(@RequestBody AddMachineInfo machine) throws Exception {
        GeneralResponse<Boolean,Object> result;
        if(machine==null)
        {
            //result = new GeneralResponse<>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.OK);
            throw new Exception("null record passed");
        }

        boolean flag;
        try {

            machineService.saveMachine(machine);
            result = new GeneralResponse<>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK,machine);

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/machine/addRecord/{name}/{datetime}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveMachineRecord(@PathVariable(name="datetime") long date, @PathVariable(name="name") String name) throws Exception {
        GeneralResponse<Boolean,Object> result;
        boolean flag;
        try {

            machineService.addTempMachineRecord(name,date);
            result = new GeneralResponse<>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/machine/addCategory")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveMachineCategory(@RequestBody AddCategory machine) throws Exception {
        GeneralResponse<Boolean,Object> result;
        if(machine==null)
        {
            throw new Exception("null record passed");//result = new GeneralResponse<>(false, "machine info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            machineService.saveMachineCategory(machine);
            result = new GeneralResponse<>(null, "Machine Category added successfully", true, System.currentTimeMillis(), HttpStatus.OK,machine);

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,machine);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/machine")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveMachineRecord(@RequestParam(name="name") String name,@RequestParam(name="speed") Double speed) throws Exception {
        GeneralResponse<Boolean,Object> result;
        if(name==null && speed == null)
        {
            throw new Exception("null record passed");
        }

        boolean flag;
        try {

            machineService.saveMachineRecord(name,speed);
            result = new GeneralResponse<>(null, "Machine Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping(value="/machine/allCategory")
    public ResponseEntity<GeneralResponse<List<GetAllCategory>,Object>> getAllMachineCategory() throws Exception {

        GeneralResponse<List<GetAllCategory>,Object> result;
        boolean flag;
        try {

            var x = machineService.getAllCategory();
            result = new GeneralResponse<>(x, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/machine/getMachine/ByCategory")
    public ResponseEntity<GeneralResponse<List<GetAllMachine>,Object>> getAllMachineByCategory(@RequestParam(name="id") Long id) throws Exception {

        GeneralResponse<List<GetAllMachine>,Object> result;
        boolean flag;
        try {

            var x = machineService.getAllMachineByCategory(id);
            result = new GeneralResponse<>(x, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/machine/all")
    public ResponseEntity<GeneralResponse<List<GetAllMachine>,Object>> getAllMachine() throws Exception {

        GeneralResponse<List<GetAllMachine>,Object> result;
        boolean flag;
        try {

            List<GetAllMachine> machineMasts = machineService.getAllMachine();
            if(machineMasts.isEmpty())
            {
                result = new GeneralResponse<>(null, "Machine Data not found ", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
            result = new GeneralResponse<>(machineMasts, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value="/machine/{id}")
    public ResponseEntity<GeneralResponse<GetAllMachine,Object>> getMachineById(@PathVariable(name="id") Long id) throws Exception {

        GeneralResponse<GetAllMachine,Object> result;
        boolean flag;
        try {

            if(id==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            GetAllMachine machineMasts = machineService.getMachineById(id);
            if(machineMasts ==null)
            {   
                result = new GeneralResponse<>(null, "Machine Data not found ", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
            result = new GeneralResponse<>(machineMasts, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/machineWithFilter")
    public ResponseEntity<GeneralResponse<GetAllMachine,Object>> getMachineByIdWithFilter(@RequestBody GetMachineByIdWithFilter getMachine) throws Exception {

        GeneralResponse<GetAllMachine,Object> result;
        boolean flag;
        try {

            if(getMachine==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            GetAllMachine machineMasts = machineService.getMachineByIdWithFilter(getMachine);
            if(machineMasts ==null)
            {
                result = new GeneralResponse<>(null, "Machine Data not found ", false, System.currentTimeMillis(), HttpStatus.OK,getMachine);
            }
            else
            result = new GeneralResponse<>(machineMasts, "Machine Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,getMachine);

            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,getMachine);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value="/machine/delete/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteMachineById(@PathVariable(name="id") Long id) throws Exception {

        GeneralResponse<Boolean,Object> result = null;
        boolean flag;
        try {

            if(id==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            flag = machineService.deleteMachineById(id);
            if(flag ==true)
            result = new GeneralResponse<>(true, "Machine Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    @DeleteMapping(value="/machine/delete/category/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteMachineCategoryById(@PathVariable(name="id") Long id) throws Exception {

        GeneralResponse<Boolean,Object> result = null;
        boolean flag;
        try {

            if(id==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            flag = machineService.deleteMachineCategoryById(id);
            if(flag ==true)
                result = new GeneralResponse<>(true, "Machine category deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/machine/update/category/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateMachineCategory(@RequestBody MachineCategory machineCategory) throws Exception {

        GeneralResponse<Boolean,Object> result = null;
        boolean flag;
        try {

            if(machineCategory==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            flag = machineService.updateMachineCategory(machineCategory);
            if(flag ==true)
                result = new GeneralResponse<>(true, "Machine category updated successfully", true, System.currentTimeMillis(), HttpStatus.OK,machineCategory);
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,machineCategory);
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping(value="/machine/get/category/isDeletable/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> getCategoryIsDeletable(@PathVariable(name="id")Long id) throws Exception {

        GeneralResponse<Boolean,Object> result;
        boolean flag;
        try {

            if(id==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            flag = machineService.getCategoryIsDeletble(id);
            if(flag ==true)
                result = new GeneralResponse<>(true, "Machine category deletable", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(false, "Machine not deletable", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
    @GetMapping(value="/machine/get/isDeletable/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> getMachineIsDeletable(@PathVariable(name="id")Long id) throws Exception {

        GeneralResponse<Boolean,Object> result;
        boolean flag;
        try {

            if(id==null)
            {
                throw new Exception("null record passed");//result = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            flag = machineService.getMachineIsDeletable(id);
            if(flag ==true)
                result = new GeneralResponse<>(true, "Machine deletable", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(false, "Machine is not deletable", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);

        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/machine/update/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateMachine(@RequestBody MachineMast machineMast) throws Exception {

        GeneralResponse<Boolean,Object> result = null;
        boolean flag;
        try {

            if(machineMast==null)
            {
                throw new Exception("null record passed");//esult = new GeneralResponse<>(null, "Machine Data can't be null ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            flag = machineService.updateMachineMast(machineMast);
            if(flag ==true)
                result = new GeneralResponse<>(true, "Machine category updated successfully", true, System.currentTimeMillis(), HttpStatus.OK,machineMast);
            logService.saveLog(result,request,debugAll);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,machineMast);
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }




}
