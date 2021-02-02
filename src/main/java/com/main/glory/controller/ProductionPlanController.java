package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
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
    public GeneralResponse<Boolean> saveProductionPlan(@RequestBody ProductionPlan productionPlan)
    {
        try {
            Long id = productionPlanService.saveProductionPlan(productionPlan);
            return new GeneralResponse<Boolean>(null, "Production Data Saved Successfully with id:"+id, true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
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



    //get all batch based on text
    /*@GetMapping(value="/getAllBatchBy/{partyId}/{qualityEntryId}/{batchId}")
    public GeneralResponse<List<BatchData>> getAllBatchByPartyIdAndQualityIdAndBatchId(@PathVariable(name ="partyId" ) Long partyId,@PathVariable(name="qualityEntryId") Long qualityEntryId,String batchId)
    {
        try {
            List<BatchData> batchDataList = productionPlanService.getAllBatch(partyId,qualityEntryId,batchId);
            if(batchDataList.isEmpty())
                throw new Exception("no data faund");

            return new GeneralResponse<>(batchDataList, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }*/

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
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
