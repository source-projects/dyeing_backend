package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.servicesImpl.ProductionPlanImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
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
            productionPlanService.saveProductionPlan(productionPlan);
            return new GeneralResponse<Boolean>(null, "Production Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
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
    public GeneralResponse<List<ProductionPlan>> getAllProductionPlan()
    {
        try {
            List<ProductionPlan> productionPlanRecord = productionPlanService.getAllProductionData();
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
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
