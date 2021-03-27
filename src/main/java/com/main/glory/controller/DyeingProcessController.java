package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingProcess.request.GetAllDyeingProcessList;
import com.main.glory.servicesImpl.DyeingProcessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DyeingProcessController extends ControllerConfig{


    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @PostMapping("/dyeingProcess")
    public ResponseEntity<GeneralResponse<Boolean>> addDyeingProcess(@RequestBody DyeingProcessMast data){
        GeneralResponse<Boolean> result;
        try {
            if(data == null){
                result = new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.OK);
            }else {
                dyeingProcessService.addDyeingProcess(data);
                result = new GeneralResponse<>(true, "Data Inserted Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @PutMapping("/dyeingProcess")
    public ResponseEntity<GeneralResponse<Boolean>> updateDyeingProcess(@RequestBody DyeingProcessMast data){
        GeneralResponse<Boolean> result;
        try {
            if(data == null){
                result = new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.OK);
            }else {
                dyeingProcessService.updateDyeingProcess(data);
                result = new GeneralResponse<>(true, "Data updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/all")
    public ResponseEntity<GeneralResponse<List<GetAllDyeingProcessList>>> getAllDyeingProcess(@RequestHeader Map<String, String> headers){
        GeneralResponse<List<GetAllDyeingProcessList>> result=null;
        try{
            List<GetAllDyeingProcessList> list = dyeingProcessService.getAllDyeingProcess(headers.get("id"));
            if(!list.isEmpty()){
                result = new GeneralResponse<>(list, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                result = new GeneralResponse<>(list, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/{id}")
    public ResponseEntity<GeneralResponse<DyeingProcessMast>> getDyeingProcessById(@PathVariable(name = "id") Long id){
        GeneralResponse<DyeingProcessMast> result=null;
        try{
            if(id==null)
                throw new Exception("id can't be null");

            DyeingProcessMast data = dyeingProcessService.getDyeingProcessById(id);
            if(data!=null){
                result = new GeneralResponse<>(data, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/dyeingProcess/isProcessExistWithName/{processName}/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> isDyeingProcessExist(@PathVariable(name = "processName")String name,@PathVariable(name = "id") Long id){
        GeneralResponse<Boolean> response;
        try{

            if(name==null)
                throw new Exception("id can't be null");

            Boolean data = dyeingProcessService.dyeingProcessExistWithName(name,id);
            if(data){
                response= new GeneralResponse<>(false, "name is not found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
            else {
                response= new GeneralResponse<>(true, "name is found", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            response= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));

    }

    @DeleteMapping("/dyeingProcess/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteProcessById(@PathVariable(name = "id") Long id){

        GeneralResponse<Boolean> result;
        try{
            if(id==null)
                throw new Exception("id can't be null");

            Boolean flag = dyeingProcessService.deleteByProcessId(id);
            if(flag){
                result = new GeneralResponse<>(true, "deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                result = new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }


}
