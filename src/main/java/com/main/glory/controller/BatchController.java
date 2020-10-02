package com.main.glory.controller;

import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchGrDetail;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.servicesImpl.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class BatchController {

    @Autowired
    private BatchServiceImpl batchService;

    @PostMapping("/batch/create")
    public GeneralResponse<Boolean> createBatch(@RequestBody BatchMast batchMast) throws Exception{
        batchService.createBatch(batchMast);
        return null;
    }

}
