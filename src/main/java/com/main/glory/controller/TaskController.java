package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.jet.request.AddJet;
import com.main.glory.model.task.TaskMast;
import com.main.glory.model.task.request.TaskDetail;
import com.main.glory.model.task.request.TaskFilter;
import com.main.glory.model.task.response.TaskResponse;
import com.main.glory.servicesImpl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskController extends ControllerConfig {

    @Autowired
    TaskServiceImpl taskService;

    @PostMapping(value="/task/add")
    public ResponseEntity<GeneralResponse<Boolean>> saveTask(@RequestBody TaskMast record) throws Exception {
        GeneralResponse<Boolean> result;
        if(record==null)
        {
            result =  new GeneralResponse<>(false, "info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            taskService.saveTask(record);
            result =  new GeneralResponse<>(null, "Task Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/task/getBy")
    public ResponseEntity<GeneralResponse<TaskResponse>> getTaskById(@RequestParam(name = "id")Long id) throws Exception {
        GeneralResponse<TaskResponse> result;
        if(id==null)
        {
            result =  new GeneralResponse<>(null, "info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            TaskResponse taskResponse = taskService.getTaskById(id);
            if(taskResponse==null)
            {
                result =  new GeneralResponse<>(taskResponse, "Data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            result =  new GeneralResponse<>(taskResponse, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/task/get")
    public ResponseEntity<GeneralResponse<List<TaskResponse>>> getAllTask(@RequestParam(name = "id")Long id) throws Exception {
        GeneralResponse<List<TaskResponse>> result;
        if(id==null)
        {
            result =  new GeneralResponse<>(null, "info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            List<TaskResponse> taskResponse = taskService.getAllTask();
            if(taskResponse.isEmpty())
            {
                result =  new GeneralResponse<>(taskResponse, "Data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                result =  new GeneralResponse<>(taskResponse, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @DeleteMapping(value="/task/deleteBy")
    public ResponseEntity<GeneralResponse<Boolean>> deleteTaskById(@RequestParam(name = "id")Long id) throws Exception {
        GeneralResponse<Boolean> result;
        if(id==null)
        {
            result =  new GeneralResponse<>(null, "info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            flag = taskService.deleteTaskById(id);
            if(flag==true)
            {
                result =  new GeneralResponse<>(true, "Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                result =  new GeneralResponse<>(false, "Unable to remove the record", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //filter task api
    @PostMapping(value="/task/getByDateAndStatus")
    public ResponseEntity<GeneralResponse<List<TaskDetail>>> getAllTaskDetailByDateAndStatus(@RequestBody TaskFilter record) throws Exception {
        GeneralResponse<List<TaskDetail>> result;

        boolean flag;
        try {

            List<TaskDetail> taskResponse = taskService.getAllTaskByDateAndStatus(record);
            if(taskResponse.isEmpty())
            {
                result =  new GeneralResponse<>(taskResponse, "Data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
            else

                result =  new GeneralResponse<>(taskResponse, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //get task by filte
    @GetMapping("/task/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<TaskDetail>>> getAllStockBatch(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id,@RequestHeader Map<String, String> headers) throws Exception {

        GeneralResponse<List<TaskDetail>> result;

        try {
            List<TaskDetail> record = null;
            switch (getBy) {
                case "assign":
                    record = taskService.getAllTaskDetail(getBy, id,headers.get("id"));
                    if (record == null) {
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
                    } else {
                        result= new GeneralResponse<>(record, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    }
                    break;

                case "assignAndCreated":
                    record = taskService.getAllTaskDetail(getBy, id,headers.get("id"));
                    if (record == null) {
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
                    } else {
                        result= new GeneralResponse<>(record, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    }
                    break;

                case "all":
                    record = taskService.getAllTaskDetail(null, null,headers.get("id"));
                    if (record == null) {
                        result= new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.OK);
                    } else {
                        result= new GeneralResponse<>(record, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    }
                    break;
                default:
                    result= new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

}
