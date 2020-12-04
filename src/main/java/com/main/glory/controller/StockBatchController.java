package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.MergeBatch;
import com.main.glory.model.StockDataBatchData.response.GetAllBatch;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
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
               return new GeneralResponse<>(false,"Stock batch not created because quality in not availble", false, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stockBatch/all/{getBy}/{id}")
    public GeneralResponse<List<GetAllStockWithPartyNameResponse>> getAllStockBatch(@PathVariable(value = "getBy")String getBy, @PathVariable(value = "id")Long id) throws Exception{
        try{
            List<GetAllStockWithPartyNameResponse> stockMast = null;
            switch (getBy) {
                case "own":
                    stockMast = stockBatchService.getAllStockBatch(getBy, id);
                    if(stockMast == null){
                        return new GeneralResponse<>(null, "Data not found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }
                    else{
                        return new GeneralResponse<>(stockMast, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    }

                case "group":
                    stockMast = stockBatchService.getAllStockBatch(getBy, id);
                    if(stockMast == null){
                        return new GeneralResponse<>(null, "Data not found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }
                    else{
                        return new GeneralResponse<>(stockMast, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    }

                case "all":
                    stockMast = stockBatchService.getAllStockBatch(null, null);
                    if(stockMast == null){
                        return new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }
                    else{
                        return new GeneralResponse<>(stockMast, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    }

                default:
                    return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
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

    @GetMapping("/stockBatch/batch/{controlId}/{batchId}")
    public GeneralResponse<List<BatchData>> getBatchById(@PathVariable(value = "batchId") String batchId,@PathVariable(value = "controlId") Long controlId){
        try{
            if(batchId!=null){
                List<BatchData> batchData = stockBatchService.getBatchById(batchId,controlId);

                    return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/stockBatch/batch/ByQualityAndParty/{qualityId}/{partyId}")
    public GeneralResponse<List<GetAllBatch>> getBatchById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId){
        try{
            if(qualityId!=null && partyId !=null){
                List<GetAllBatch> batchData = stockBatchService.getBatchByPartyAndQuality(qualityId,partyId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
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
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/stockBatch/MergeBatch")
    public GeneralResponse<Boolean> updateBatchMerge(@RequestBody List<MergeBatch> batchData1) {
        try {
            stockBatchService.updateBatchForMerge(batchData1);
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
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
