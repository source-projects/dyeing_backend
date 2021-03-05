package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.productionPlan.request.AddProductionWithJet;
import com.main.glory.model.productionPlan.request.GetAllProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.servicesImpl.ProductionPlanImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductionPlanController extends ControllerConfig {

    @Autowired
    ProductionPlanImpl productionPlanService;

    @PostMapping(value="/productionPlan/")
    public ResponseEntity<GeneralResponse<Long>> saveProductionPlan(@RequestBody ProductionPlan productionPlan)
    {
        GeneralResponse<Long> result =null;
        try {
            Long id = productionPlanService.saveProductionPlan(productionPlan);
            result= new GeneralResponse<>(id, "Production Data Saved Successfully with id:"+id, true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @Transactional
    @PostMapping(value="/productionPlan/productionPlanWithJet/")
    public ResponseEntity<GeneralResponse<Long>> productionPlanWithJet(@RequestBody AddProductionWithJet productionPlan)
    {
        GeneralResponse<Long> result;
        try {
            Long id = productionPlanService.saveProductionPlanWithJet(productionPlan);
            result= new GeneralResponse<>(id, "Production Data Saved Successfully with id:"+id, true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get batch detail by production and batch id
    @GetMapping(value="/productionPlan/getBatchdDetailByProductionAndBatch/{productionId}/{batchId}")
    public ResponseEntity<GeneralResponse<GetBatchDetailByProduction>> getBatchDetailByProductinIdAndBatchId(@PathVariable(name="productionId")Long productionId,@PathVariable(name = "batchId") String batchId)
    {
        GeneralResponse<GetBatchDetailByProduction> result;
        try {
            GetBatchDetailByProduction data = productionPlanService.getBatchDetailByProductionAndBatchId(productionId,batchId);
            if(data!=null)
                result = new GeneralResponse<>(data, " Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(data, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping(value="/productionPlan/updateProductionPlan/")
    public ResponseEntity<GeneralResponse<Boolean>> updateProductionPlan(@RequestBody ProductionPlan productionPlan)
    {
        GeneralResponse<Boolean> result;
        try {
            productionPlanService.updateProductionPlan(productionPlan);
            result = new GeneralResponse<Boolean>(null, "updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value="/productionPlan/{id}")
    public ResponseEntity<GeneralResponse<ProductionPlan>> getProductionPlan(@PathVariable(name="id")Long id)
    {
        GeneralResponse<ProductionPlan> result;
        try {
            ProductionPlan productionPlanRecord = productionPlanService.getProductionData(id);
            result =  new GeneralResponse<ProductionPlan>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/productionPlan/all")
    public ResponseEntity<GeneralResponse<List<GetAllProductionWithShadeData>>> getAllProductionWithoutJetPlan()
    {
        GeneralResponse<List<GetAllProductionWithShadeData>> result;
        try {
            List<GetAllProductionWithShadeData> productionPlanRecord = productionPlanService.getAllProductionData();
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            result = new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/productionPlan/withAndWithoutPlan/all")
    public ResponseEntity<GeneralResponse<List<GetAllProductionWithShadeData>>> getAllProduction()
    {
        GeneralResponse<List<GetAllProductionWithShadeData>> result;
        try {
            List<GetAllProductionWithShadeData> productionPlanRecord = productionPlanService.getAllProductionDataWithAndWithoutPlan();
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            result = new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get all production without filter]
    @GetMapping(value="/productionPlan/allProductionWithoutFilter")
    public ResponseEntity<GeneralResponse<List<GetAllProduction>>> allProductionWithoutFilter()
    {
        GeneralResponse<List<GetAllProduction>> result;
        try {
            List<GetAllProduction> productionPlanRecord = productionPlanService.getAllProductionWithoutFilter();
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            result= new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get production by party and quality who are not added in jet yet
    @GetMapping(value="/productionPlan/getProductionByPartyAndQuality/{partyId}/{qualityEntryId}")
    public ResponseEntity<GeneralResponse<List<ProductionPlan>>> getProductionByPartyAndQuality(@PathVariable(name = "partyId") Long partyId,@PathVariable(name = "qualityEntryId") Long qualityEntryId)
    {
        GeneralResponse<List<ProductionPlan>> result;
        try {
            List<ProductionPlan> productionPlanRecord = productionPlanService.getAllProductionListByPartyAndQuality(partyId,qualityEntryId);
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            result= new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @DeleteMapping(value="/productionPlan/deleteBy/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteById(@PathVariable(name="id")Long id)
    {
        GeneralResponse<Boolean> result;
        try {
            Boolean flag = productionPlanService.deleteById(id);
            if(flag)
            result =  new GeneralResponse<Boolean>(true, "Data deleted Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<Boolean>(true, "Data not found ", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


}
