package com.main.glory.controller;

import com.main.glory.model.BatchMast;
import com.main.glory.servicesImpl.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class BatchController {

    @Autowired
    private BatchServiceImpl batchService;

    @PostMapping("/batch")
    private int addBatch(@RequestBody BatchMast batchMast) throws Exception {
        return batchService.saveBatch(batchMast);
    }
}
