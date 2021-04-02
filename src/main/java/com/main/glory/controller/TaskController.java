package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.jet.request.AddJet;
import com.main.glory.model.task.TaskMast;
import com.main.glory.servicesImpl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController extends ControllerConfig {

    @Autowired
    TaskServiceImpl taskService;

    @PostMapping(value="/task/add")
    public GeneralResponse<Boolean> saveJet(@RequestBody TaskMast record) throws Exception {
        if(record==null)
        {
            return new GeneralResponse<Boolean>(false, "info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            taskService.saveTask(record);
            return new GeneralResponse<Boolean>(null, "Task Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

}
