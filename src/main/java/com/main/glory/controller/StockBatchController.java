package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StockBatchController extends ControllerConfig {

    @Autowired
    private BatchImpl batchService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;


    @PostMapping("/stockBatch")
    public GeneralResponse<Boolean> createBatch(@RequestBody StockMast stockMast) throws Exception{
        try{
           Boolean flag = stockBatchService.saveStockBatch(stockMast);
           if(flag==true)
            return new GeneralResponse<>(true,"Stock batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
           else
               return new GeneralResponse<>(false,"Stock batch not created because quality in not availble", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stockBatch/all")
    public GeneralResponse<List<StockMast>> getAllStockBatch() throws Exception{
        try{
            List<StockMast> stockMast = stockBatchService.getAllStockBatch();
            if(stockMast == null){
                return new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            else{
                return new GeneralResponse<List<StockMast>>(stockMast, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stockBatchQty/ByQualityId")
    public GeneralResponse<List<BatchData>> ByQualityId(@PathVariable(value = "qualityId") Long qualityId) throws Exception{
        try{

            List<BatchData> stockMast = stockBatchService.getAllBatchByQuality(qualityId);
            if(stockMast == null){
                return new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            else{
                return new GeneralResponse<List<BatchData>>(stockMast, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stockBatch/{id}")
    public GeneralResponse<StockMast> getStockMastById(@PathVariable(value = "id") Long id){
        try{
            if(id!=null){
                Optional<StockMast> stockMast = stockBatchService.getStockBatchById(id);
                if(stockMast.isPresent()){
                    return new GeneralResponse<StockMast>(stockMast.get(), "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                }else{
                    return new GeneralResponse<StockMast>(null, "no data found for id: "+id, false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                }
            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/stockBatch")
    public GeneralResponse<Boolean> updateStockBatch(@RequestBody StockMast stockMast) {
        try {
            stockBatchService.updateBatch(stockMast);
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/stockBatch/{id}")
    public GeneralResponse<Boolean> deleteStockBatch(@PathVariable(value = "id") Long id){
        try{
            stockBatchService.deleteStockBatch(id);
            return new GeneralResponse<>(true,"stockBatch deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
}
