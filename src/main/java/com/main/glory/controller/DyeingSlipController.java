package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.Constant;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.dyeingSlip.request.AddAddtionalSlip;
import com.main.glory.model.dyeingSlip.request.GetItemByShadeAndBatch;
import com.main.glory.model.dyeingSlip.request.SlipFormatData;
import com.main.glory.model.dyeingSlip.responce.BatchResponseWithSlip;
import com.main.glory.model.dyeingSlip.responce.GetAllAdditionalDyeingSlip;
import com.main.glory.model.dyeingSlip.responce.ItemListForDirectDyeing;
import com.main.glory.servicesImpl.DyeingSlipServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DyeingSlipController extends ControllerConfig {

    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    Constant constant;

    @PutMapping("/dyeingSlip")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateDyeingSlip(@RequestBody DyeingSlipMast data){
        GeneralResponse<Boolean,Object> result;
        try {
            if(data == null){
                result = new GeneralResponse<>(false, constant.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,data);
            }else {
                dyeingSlipService.updateDyeingSlip(data);
                result = new GeneralResponse<>(true, constant.DyeingSlip_Updated, true, System.currentTimeMillis(), HttpStatus.OK,data);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,data);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingSlip/{batchId}/{productionId}")
    public ResponseEntity<GeneralResponse<SlipFormatData,Object>> getDyeingSlipByBatchIdProductionId(@PathVariable(name = "batchId") String batchId, @PathVariable(name = "productionId") Long productionId){
        GeneralResponse<SlipFormatData,Object> result;
        try {
            if(batchId == null || productionId == null){
                throw new Exception(constant.Null_Record_Passed);
            }else {

                SlipFormatData data = dyeingSlipService.getDyeingSlipByBatchStockId(batchId, productionId);
                if(data!=null)
                result = new GeneralResponse<>(data, constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                else
                    result = new GeneralResponse<>(null, constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingSlip/all")
    public ResponseEntity<GeneralResponse<List<DyeingSlipMast>,Object>> getAllDyeingSlip(){
        GeneralResponse<List<DyeingSlipMast>,Object> result;
        try {

                List<DyeingSlipMast> data = dyeingSlipService.getAllDyeingSlip();
                if(data!=null)
                    result = new GeneralResponse<>(data, constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                else
                    result = new GeneralResponse<>(null, constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingSlip/additionalDyeingslip/all")
    public ResponseEntity<GeneralResponse<List<GetAllAdditionalDyeingSlip>,Object>> getAllAddtionalDyeignSlip(){
        GeneralResponse<List<GetAllAdditionalDyeingSlip>,Object> result;
        try {

            List<GetAllAdditionalDyeingSlip> data = dyeingSlipService.getAllAdditionalDyeingSlip();
            if(!data.isEmpty())
                result = new GeneralResponse<>(data, constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(null, constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //add addition dyeing slip
    @PostMapping("/dyeingSlip/add/additionalDyeingSlip/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> addAddtionalDyeingSlip(@RequestBody AddAddtionalSlip addAdditionDyeingSlipModel){
        GeneralResponse<Boolean,Object> result;
        try {
            if(addAdditionDyeingSlipModel ==null)
            result = new GeneralResponse<>(false, constant.Null_Record_Passed,false,System.currentTimeMillis(),HttpStatus.OK,addAdditionDyeingSlipModel);

            dyeingSlipService.addAdditionalSlipData(addAdditionDyeingSlipModel);
            result = new GeneralResponse<>(true, constant.DyeingSlip_Added, true, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);

            logService.saveLog(result,request,debugAll);


        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //update addition dyeing slip
    @PutMapping("/dyeingSlip/update/additionalDyeingSlip/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateAddtionalDyeingSlip(@RequestBody AddAddtionalSlip addAdditionDyeingSlipModel){
        GeneralResponse<Boolean,Object> result;
        try {
            if(addAdditionDyeingSlipModel ==null)
                result = new GeneralResponse<>(false, Constant.Null_Record_Passed,false,System.currentTimeMillis(),HttpStatus.OK,addAdditionDyeingSlipModel);

           dyeingSlipService.updateAddtionalDyeingSlip(addAdditionDyeingSlipModel);

           result = new GeneralResponse<>(true, Constant.DyeingSlip_Updated, true, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get addition dyeing slip
    @GetMapping("/dyeingSlip/getAdditionalDyeingSlipBy/{id}")
    public ResponseEntity<GeneralResponse<GetAllAdditionalDyeingSlip,Object>> getAdditionalDyeingSlipBy(@PathVariable(name = "id") Long id){
        GeneralResponse<GetAllAdditionalDyeingSlip,Object> result;
        try {
            if(id ==null)
                throw new Exception(Constant.Null_Record_Passed);

            GetAllAdditionalDyeingSlip data = dyeingSlipService.getAdditionalDyeingSlipById(id);
            if(data!=null)
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    //get addition dyeing slip
    @GetMapping("/dyeingSlip/getAdditionalDyeingSlipByForPrint")
    public ResponseEntity<GeneralResponse<BatchResponseWithSlip,Object>> getAdditionalDyeingSlipByForPrint(@RequestParam(name = "id") Long id){
        GeneralResponse<BatchResponseWithSlip,Object> result;
        try {
            if(id ==null)
                throw new Exception("null record passed");

            BatchResponseWithSlip data = dyeingSlipService.getAdditionalDyeingSlipForPrintById(id);
            if(data!=null)
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @DeleteMapping("/dyeingSlip/deleteAdditionalDyeingSlipBy/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteAdditionalDyeingSlipBy(@PathVariable(name = "id") Long id){
        GeneralResponse<Boolean,Object> result;
        try {
            if(id ==null)
                throw new Exception(Constant.Null_Record_Passed);

            Boolean data = dyeingSlipService.deleteAdditionalDyeingSlipById(id);
            if(data)
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/dyeingSlip/directDyeingSlip/all")
    public ResponseEntity<GeneralResponse<List<GetAllAdditionalDyeingSlip>,Object>> getAllDirectDyeignSlip(){
        GeneralResponse<List<GetAllAdditionalDyeingSlip>,Object> result;
        try {

            List<GetAllAdditionalDyeingSlip> data = dyeingSlipService.getAllDirectDyeignSlip();
            if(!data.isEmpty())
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(null, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get direct dyeing slip
    @GetMapping("/dyeingSlip/getDirectDyeingSlipBy/{id}")
    public ResponseEntity<GeneralResponse<GetAllAdditionalDyeingSlip,Object>> getDirectDyeingSlipBy(@PathVariable(name = "id") Long id){
        GeneralResponse<GetAllAdditionalDyeingSlip,Object> result;
        try {
            if(id ==null)
                throw new Exception(Constant.Null_Record_Passed);

            GetAllAdditionalDyeingSlip data = dyeingSlipService.getDirectDyeingSlipById(id);
            if(data!=null)
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //update direct dyeing slip
    @PutMapping("/dyeingSlip/update/directDyeingSlip/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateDirectDyeingSlip(@RequestBody AddAddtionalSlip addAdditionDyeingSlipModel) {
        GeneralResponse<Boolean,Object> result;
        try {
            if (addAdditionDyeingSlipModel == null)
                throw new Exception(Constant.Null_Record_Passed);


            dyeingSlipService.updateDirectDyeingSlip(addAdditionDyeingSlipModel);

            result = new GeneralResponse<>(true, Constant.DyeingSlip_Updated, true, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    /*@DeleteMapping("/dyeingSlip/deleteAdditionalDyeingSlipBy/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteAdditionalDyeingSlipBy(@PathVariable(name = "id") Long id){
        GeneralResponse<Boolean> result;
        try {
            if(id ==null)
                result = new GeneralResponse<>(null,"info can't be null",false,System.currentTimeMillis(),HttpStatus.OK);

            Boolean data = dyeingSlipService.deleteAdditionalDyeingSlipById(id);
            if(data)
                result = new GeneralResponse<>(data, "Data deleted Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(data, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }*/




    //redyeing slip

    //we had use the same api for adding the additional dyeing slip and redyeing slip
    //not required to make api for the "" add redyeing slip than can be done by add addtional dyeing slip
    // make only get view delete redyeing slip api

    @GetMapping("/dyeingSlip/reDyeingSlip/all")
    public ResponseEntity<GeneralResponse<List<GetAllAdditionalDyeingSlip>,Object>> getAllReDirectDyeingSlip(){
        GeneralResponse<List<GetAllAdditionalDyeingSlip>,Object> result;
        try {

            List<GetAllAdditionalDyeingSlip> data = dyeingSlipService.getAllReDyeignSlip();
            if(!data.isEmpty())
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(null, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    //get re dyeing slip
    @GetMapping("/dyeingSlip/getReDyeingSlipBy")
    public ResponseEntity<GeneralResponse<GetAllAdditionalDyeingSlip,Object>> getReDyeingSlipBy(@RequestParam Long id){
        GeneralResponse<GetAllAdditionalDyeingSlip,Object> result;
        try {
            if(id ==null)
                throw new Exception(Constant.Null_Record_Passed);//result = new GeneralResponse<>(null,"info can't be null",false,System.currentTimeMillis(),HttpStatus.OK);

            GetAllAdditionalDyeingSlip data = dyeingSlipService.getReDyeingSlipById(id);
            if(data!=null)
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(data, Constant.DyeingSlip_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());


            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //update direct dyeing slip
    @PutMapping("/dyeingSlip/update/reDyeingSlip/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateReDyeingSlip(@RequestBody AddAddtionalSlip addAdditionDyeingSlipModel) {
        GeneralResponse<Boolean,Object> result;
        try {
            if (addAdditionDyeingSlipModel == null)
                throw new Exception(Constant.Null_Record_Passed);//result = new GeneralResponse<>(false, "info can't be null", false, System.currentTimeMillis(), HttpStatus.OK);

            dyeingSlipService.updateReDyeingSlip(addAdditionDyeingSlipModel);

            result = new GeneralResponse<>(true, Constant.DyeingSlip_Updated, true, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);
            logService.saveLog(result,request,debugAll);


        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,addAdditionDyeingSlipModel);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    //get shade item for the direct dyeing slip

    @PostMapping("/dyeingSlip/getItemListByShadeAndBatch")
    public ResponseEntity<GeneralResponse<List<ItemListForDirectDyeing>,Object>> getItemListByShadeAndBatch(@RequestBody GetItemByShadeAndBatch record) {
        GeneralResponse<List<ItemListForDirectDyeing>,Object> result;
        try {
            if (record == null)
                throw new Exception(Constant.Null_Record_Passed);//result = new GeneralResponse<>(null, "info can't be null", false, System.currentTimeMillis(), HttpStatus.OK);

            List<ItemListForDirectDyeing> list  = dyeingSlipService.getItemListByShadeAndBatch(record);

            if(!list.isEmpty())
            result = new GeneralResponse<>(list, Constant.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);
            else
            result = new GeneralResponse<>(list, Constant.SupplierRate_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }





}
