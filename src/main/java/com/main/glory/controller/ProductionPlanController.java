package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.productionPlan.request.AddDirectBatchToJet;
import com.main.glory.model.productionPlan.request.AddProductionWithJet;
import com.main.glory.model.productionPlan.request.GetAllProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.ProductionPlanImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductionPlanController extends ControllerConfig {

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;


    @PostMapping(value="/productionPlan/")
    public ResponseEntity<GeneralResponse<Long,Object>> saveProductionPlan(@RequestBody ProductionPlan productionPlan,@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<Long,Object> result =null;
        try {
            Long id = productionPlanService.saveProductionPlan(productionPlan);
            result= new GeneralResponse<>(id, CommonMessage.Production_Added, true, System.currentTimeMillis(), HttpStatus.OK,productionPlan);
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,productionPlan);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @Transactional
    @PostMapping(value="/productionPlan/directDyeingSlip")
    public ResponseEntity<GeneralResponse<Long,Object>> saveDirectBatchToJet(@RequestBody AddDirectBatchToJet productionPlan, @RequestHeader Map<String, String> headers)
    {
        GeneralResponse<Long,Object> result =null;
        try {
            Long id  = productionPlanService.saveDirectDyeingSlip(productionPlan);
            result= new GeneralResponse<>(id, CommonMessage.DyeingSlip_Added, true, System.currentTimeMillis(), HttpStatus.OK,productionPlan);
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,productionPlan);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @Transactional
    @PostMapping(value="/productionPlan/productionPlanWithJet/")
    public ResponseEntity<GeneralResponse<Long,Object>> productionPlanWithJet(@RequestBody AddProductionWithJet productionPlan)
    {
        GeneralResponse<Long,Object> result;
        try {
            Long id = productionPlanService.saveProductionPlanWithJet(productionPlan);
            result= new GeneralResponse<>(id, CommonMessage.Production_Added, true, System.currentTimeMillis(), HttpStatus.OK,productionPlan);
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,productionPlan);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get batch detail by production and batch id
    @GetMapping(value="/productionPlan/getBatchdDetailByProductionAndBatch/{productionId}/{batchId}")
    public ResponseEntity<GeneralResponse<GetBatchDetailByProduction,Object>> getBatchDetailByProductinIdAndBatchId(@PathVariable(name="productionId")Long productionId,@PathVariable(name = "batchId") String batchId)
    {
        GeneralResponse<GetBatchDetailByProduction,Object> result;
        try {
            GetBatchDetailByProduction data = productionPlanService.getBatchDetailByProductionAndBatchId(productionId,batchId);
            if(data!=null)
                result = new GeneralResponse<>(data, CommonMessage.Production_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(data, CommonMessage.Production_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping(value="/productionPlan/updateProductionPlan/")
    public ResponseEntity<GeneralResponse<Boolean, Object>> updateProductionPlan(@RequestBody ProductionPlan productionPlan, @RequestHeader Map<String, String> headers)
    {
        GeneralResponse<Boolean,Object> result;
        try {
            productionPlanService.updateProductionPlan(productionPlan,headers.get("id"));
            result = new GeneralResponse<>(null, CommonMessage.Production_Updated, true, System.currentTimeMillis(), HttpStatus.OK,productionPlan);
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,productionPlan);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value="/productionPlan/{id}")
    public ResponseEntity<GeneralResponse<ProductionPlan,Object>> getProductionPlan(@PathVariable(name="id")Long id)
    {
        GeneralResponse<ProductionPlan,Object> result;
        try {
            ProductionPlan productionPlanRecord = productionPlanService.getProductionData(id);
            result =  new GeneralResponse<>(productionPlanRecord, CommonMessage.Production_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/productionPlan/all")
    public ResponseEntity<GeneralResponse<List<GetAllProductionWithShadeData>,Object>> getAllProductionWithoutJetPlan(@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<List<GetAllProductionWithShadeData>,Object> result;
        try {
            List<GetAllProductionWithShadeData> productionPlanRecord = productionPlanService.getAllProductionData(headers.get("id"));
            if(productionPlanRecord.isEmpty())
                throw new Exception(CommonMessage.Production_Not_Found);

            result = new GeneralResponse<>(productionPlanRecord, CommonMessage.Production_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/productionPlan/withAndWithoutPlan/all")
    public ResponseEntity<GeneralResponse<List<GetAllProductionWithShadeData>,Object>> getAllProduction()
    {
        GeneralResponse<List<GetAllProductionWithShadeData>,Object> result;
        try {
            List<GetAllProductionWithShadeData> productionPlanRecord = productionPlanService.getAllProductionDataWithAndWithoutPlan();
            if(productionPlanRecord.isEmpty())
                throw new Exception(CommonMessage.Production_Not_Found);

            result = new GeneralResponse<>(productionPlanRecord, CommonMessage.Production_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get all production without filter]
    @GetMapping(value="/productionPlan/allProductionWithoutFilter")
    public ResponseEntity<GeneralResponse<List<GetAllProduction>,Object>> allProductionWithoutFilter()
    {
        GeneralResponse<List<GetAllProduction>,Object> result;
        try {
            List<GetAllProduction> productionPlanRecord = productionPlanService.getAllProductionWithoutFilter();
            if(productionPlanRecord.isEmpty())
                throw new Exception(CommonMessage.Production_Not_Found);

            result= new GeneralResponse<>(productionPlanRecord, CommonMessage.Production_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get production by party and quality who are not added in jet yet
    @GetMapping(value="/productionPlan/getProductionByPartyAndQuality/{partyId}/{qualityEntryId}")
    public ResponseEntity<GeneralResponse<List<ProductionPlan>,Object>> getProductionByPartyAndQuality(@PathVariable(name = "partyId") Long partyId,@PathVariable(name = "qualityEntryId") Long qualityEntryId)
    {
        GeneralResponse<List<ProductionPlan>,Object> result;
        try {
            List<ProductionPlan> productionPlanRecord = productionPlanService.getAllProductionListByPartyAndQuality(partyId,qualityEntryId);
            if(productionPlanRecord.isEmpty())
                throw new Exception(CommonMessage.Production_Not_Found);

            result= new GeneralResponse<>(productionPlanRecord, CommonMessage.Production_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @DeleteMapping(value="/productionPlan/deleteBy/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteById(@PathVariable(name="id")Long id)
    {
        GeneralResponse<Boolean,Object> result;
        try {
            Boolean flag = productionPlanService.deleteById(id);
            if(flag)
            result =  new GeneralResponse<>(true, CommonMessage.Production_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(true, CommonMessage.Production_Unable_Deleted, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


}
