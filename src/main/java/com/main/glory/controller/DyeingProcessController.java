package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingProcess.request.GetAllDyeingProcessList;
import com.main.glory.servicesImpl.DyeingProcessServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DyeingProcessController extends ControllerConfig{


    CommonMessage commonMessage;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @PostMapping("/dyeingProcess")
    public ResponseEntity<GeneralResponse<Boolean,Object>> addDyeingProcess(@RequestBody DyeingProcessMast data){
        GeneralResponse<Boolean,Object> result;
        try {
            if(data == null){
                result = new GeneralResponse<>(false, commonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,data);
            }else {
                dyeingProcessService.addDyeingProcess(data);
                result = new GeneralResponse<>(true, commonMessage.DyeingProcess_Added, true, System.currentTimeMillis(), HttpStatus.CREATED,data);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,data);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @PutMapping("/dyeingProcess")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateDyeingProcess(@RequestBody DyeingProcessMast data){
        GeneralResponse<Boolean,Object> result;
        try {
            if(data == null){
                result = new GeneralResponse<>(false, commonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,data);
            }else {
                dyeingProcessService.updateDyeingProcess(data);
                result = new GeneralResponse<>(true, commonMessage.DyeingProcess_Updated, true, System.currentTimeMillis(), HttpStatus.OK,data);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,data);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/all")
    public ResponseEntity<GeneralResponse<List<GetAllDyeingProcessList>,Object>> getAllDyeingProcess(@RequestHeader Map<String, String> headers){
        GeneralResponse<List<GetAllDyeingProcessList>,Object> result=null;
        try{
            List<GetAllDyeingProcessList> list = dyeingProcessService.getAllDyeingProcess(headers.get("id"));
            if(!list.isEmpty()){
                result = new GeneralResponse<>(list, commonMessage.DyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
            {
                result = new GeneralResponse<>(list, commonMessage.DyeingProcess_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/{id}")
    public ResponseEntity<GeneralResponse<DyeingProcessMast,Object>> getDyeingProcessById(@PathVariable(name = "id") Long id){
        GeneralResponse<DyeingProcessMast,Object> result=null;
        try{
            if(id==null)
                throw new Exception(commonMessage.Null_Record_Passed);

            DyeingProcessMast data = dyeingProcessService.getDyeingProcessById(id);
            if(data!=null){
                result = new GeneralResponse<>(data, commonMessage.DyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/dyeingProcess/isProcessExistWithName/{processName}/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> isDyeingProcessExist(@PathVariable(name = "processName")String name,@PathVariable(name = "id") Long id){
        GeneralResponse<Boolean,Object> result;
        try{

            if(name==null)
                throw new Exception(commonMessage.Null_Record_Passed);

            Boolean data = dyeingProcessService.dyeingProcessExistWithName(name,id);
            if(data){
                result = new GeneralResponse<>(false, commonMessage.DyeingProcess_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else {
                result= new GeneralResponse<>(true, commonMessage.DyeingProcess_Name_Exist, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @DeleteMapping("/dyeingProcess/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteProcessById(@PathVariable(name = "id") Long id){

        GeneralResponse<Boolean,Object> result;
        try{
            if(id==null)
                throw new Exception(commonMessage.Null_Record_Passed);

            Boolean flag = dyeingProcessService.deleteByProcessId(id);
            if(flag){
                result = new GeneralResponse<>(true, commonMessage.DyeingProcess_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
                result = new GeneralResponse<>(null, commonMessage.DyeingProcess_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }


}
