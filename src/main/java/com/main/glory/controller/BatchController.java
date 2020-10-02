package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchGrDetail;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.servicesImpl.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BatchController extends ControllerConfig {

    @Autowired
    private BatchServiceImpl batchService;

    @PostMapping("/batch")
    public GeneralResponse<Boolean> createBatch(@RequestBody BatchMast batchMast) throws Exception{
        try{
            batchService.createBatch(batchMast);
            return new GeneralResponse<>(true,"batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){

            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

}
