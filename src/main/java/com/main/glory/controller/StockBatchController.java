package com.main.glory.controller;


import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.Batch;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StockBatchController {

    @Autowired
    private BatchImpl batchService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;


    @PostMapping("/stockBatchCreate")
    public GeneralResponse<Boolean> createBatch(@RequestBody Batch batch) throws Exception{
        try{

           //  stockBatchService.createStockBatch(,batch);
            batchService.saveBatch(batch);

            return new GeneralResponse<>(true,"Stock batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){

            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
