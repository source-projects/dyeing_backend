package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingProcess.request.GetAllDyeingProcessList;
import com.main.glory.servicesImpl.DyeingProcessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DyeingProcessController extends ControllerConfig{


    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @PostMapping("/dyeingProcess")
    public GeneralResponse<Boolean> addDyeingProcess(@RequestBody DyeingProcessMast data){
        try {
            if(data == null){
                return new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }else {
                dyeingProcessService.addDyeingProcess(data);
                return new GeneralResponse<>(true, "Data Inserted Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/dyeingProcess/update")
    public GeneralResponse<Boolean> updateDyeingProcess(@RequestBody DyeingProcessMast data){
        try {
            if(data == null){
                return new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }else {
                dyeingProcessService.updateDyeingProcess(data);
                return new GeneralResponse<>(true, "Data updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dyeingProcess/all")
    public GeneralResponse<List<GetAllDyeingProcessList>> getAllDyeingProcess(){
        try{
            List<GetAllDyeingProcessList> list = dyeingProcessService.getAllDyeingProcess();
            if(!list.isEmpty()){
                return new GeneralResponse<>(list, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/dyeingProcess/{id}")
    public GeneralResponse<DyeingProcessMast> getDyeingProcessById(@PathVariable(name = "id") Long id){
        try{
            if(id==null)
                throw new Exception("id can't be null");

            DyeingProcessMast data = dyeingProcessService.getDyeingProcessById(id);
            if(data!=null){
                return new GeneralResponse<>(data, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/dyeingProcess/deleteProcessById/{id}")
    public GeneralResponse<Boolean> deleteProcessById(@PathVariable(name = "id") Long id){
        try{
            if(id==null)
                throw new Exception("id can't be null");

            Boolean flag = dyeingProcessService.deleteByProcessId(id);
            if(flag){
                return new GeneralResponse<>(true, "deleted successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }
            else
                return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }


}
