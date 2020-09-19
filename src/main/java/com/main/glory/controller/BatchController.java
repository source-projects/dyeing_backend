package com.main.glory.controller;

import com.main.glory.model.BatchGrDetail;
import com.main.glory.model.BatchMast;
import com.main.glory.servicesImpl.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class BatchController {

    @Autowired
    private BatchServiceImpl batchService;

    @PostMapping("/batch")
    private int addBatch(@RequestBody BatchMast batchMast) throws Exception {

        return batchService.saveBatch(batchMast);
    }

    @GetMapping("/batch-list")
    public List<BatchMast>getBatchList() throws Exception
    {
        List<BatchMast> batchList=batchService.getAllBatch();
        if(batchList.isEmpty())
        {
            System.out.println("NO Record Found");
            return null;
        }else
            return batchList;
    }

    @GetMapping(value="/get-fab-stock-detail/{id}")
    public List<BatchGrDetail> getGrDetailsBySelectedQualityId(@PathVariable("id") Long id)
    {
        var findData=batchService.getGrById(id);
        return findData;
    }
}
