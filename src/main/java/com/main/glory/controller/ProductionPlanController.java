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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductionPlanController extends ControllerConfig {

    @Autowired
    ProductionPlanImpl productionPlanService;

    @PostMapping(value="/productionPlan/")
    public GeneralResponse<Long> saveProductionPlan(@RequestBody ProductionPlan productionPlan)
    {
        try {
            Long id = productionPlanService.saveProductionPlan(productionPlan);
            return new GeneralResponse<>(id, "Production Data Saved Successfully with id:"+id, true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/productionPlanWithJet/")
    public GeneralResponse<Long> productionPlanWithJet(@RequestBody AddProductionWithJet productionPlan)
    {
        try {
            Long id = productionPlanService.saveProductionPlanWithJet(productionPlan);
            return new GeneralResponse<>(id, "Production Data Saved Successfully with id:"+id, true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get batch detail by production and batch id
    @GetMapping(value="/productionPlan/getBatchdDetailByProductionAndBatch/{productionId}/{batchId}")
    public GeneralResponse<GetBatchDetailByProduction> getBatchDetailByProductinIdAndBatchId(@PathVariable(name="productionId")Long productionId,@PathVariable(name = "batchId") String batchId)
    {
        GeneralResponse<GetBatchDetailByProduction> result;
        try {
            GetBatchDetailByProduction data = productionPlanService.getBatchDetailByProductionAndBatchId(productionId,batchId);
            if(data!=null)
                result= new GeneralResponse<>(data, " Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            else
                result= new GeneralResponse<>(data, " data not found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }



    @PutMapping(value="/updateProductionPlan/")
    public GeneralResponse<Boolean> updateProductionPlan(@RequestBody ProductionPlan productionPlan)
    {
        try {
            productionPlanService.updateProductionPlan(productionPlan);
            return new GeneralResponse<Boolean>(null, "updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/productionPlan/{id}")
    public GeneralResponse<ProductionPlan> getProductionPlan(@PathVariable(name="id")Long id)
    {
        try {
            ProductionPlan productionPlanRecord = productionPlanService.getProductionData(id);
            return new GeneralResponse<ProductionPlan>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/productionPlan/all")
    public GeneralResponse<List<GetAllProductionWithShadeData>> getAllProductionWithoutJetPlan()
    {
        try {
            List<GetAllProductionWithShadeData> productionPlanRecord = productionPlanService.getAllProductionData();
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            return new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/productionPlan/withAndWithoutPlan/all")
    public GeneralResponse<List<GetAllProductionWithShadeData>> getAllProduction()
    {
        try {
            List<GetAllProductionWithShadeData> productionPlanRecord = productionPlanService.getAllProductionDataWithAndWithoutPlan();
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            return new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get all production without filter]
    @GetMapping(value="/productionPlan/allProductionWithoutFilter")
    public GeneralResponse<List<GetAllProduction>> allProductionWithoutFilter()
    {
        try {
            List<GetAllProduction> productionPlanRecord = productionPlanService.getAllProductionWithoutFilter();
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            return new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get production by party and quality who are not added in jet yet
    @GetMapping(value="/productionPlan/getProductionByPartyAndQuality/{partyId}/{qualityEntryId}")
    public GeneralResponse<List<ProductionPlan>> getProductionByPartyAndQuality(@PathVariable(name = "partyId") Long partyId,@PathVariable(name = "qualityEntryId") Long qualityEntryId)
    {
        try {
            List<ProductionPlan> productionPlanRecord = productionPlanService.getAllProductionListByPartyAndQuality(partyId,qualityEntryId);
            if(productionPlanRecord.isEmpty())
                throw new Exception("no data faund");

            return new GeneralResponse<>(productionPlanRecord, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(value="/productionPlan/deleteBy/{id}")
    public GeneralResponse<Boolean> deleteById(@PathVariable(name="id")Long id)
    {
        try {
            Boolean flag = productionPlanService.deleteById(id);
            if(flag)
            return new GeneralResponse<Boolean>(true, "Data deleted Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<Boolean>(true, "Data not found ", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
    }


}
