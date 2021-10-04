package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.machine.request.PaginatedData;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.AddSupplier;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.model.supplier.responce.*;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SupplierController extends ControllerConfig {

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll;



    @GetMapping("/supplier/isSupplierNameExists/{name}/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> isUniqueSupplierName(@PathVariable("name") String name, @PathVariable("id") Long id){
        GeneralResponse<Boolean,Object> result;
        try{
            Boolean isPresent = supplierService.isUniqueName(id, name);
            result =  new GeneralResponse<>(isPresent, ConstantFile.Supplier_Found+isPresent, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }catch (Exception e){
            e.printStackTrace();
            result =new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/supplier")
    public ResponseEntity<GeneralResponse<Boolean,Object>> addSupplier(@RequestBody AddSupplier addSupplier,@RequestHeader Map<String, String> headers){
        GeneralResponse<Boolean,Object> result;
        try{
            Boolean flag = supplierService.addSupplier(addSupplier,headers.get("id"));
            if(flag) {
                result= new GeneralResponse<>(true, ConstantFile.Supplier_Added, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(false, ConstantFile.Supplier_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PostMapping("/supplier/allPaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<GetSupplierPaginatedData>,Object>> getSupplierPaginatedData(@RequestBody GetBYPaginatedAndFiltered requestParam){
        GeneralResponse<FilterResponse<GetSupplierPaginatedData>,Object> result;
        try{
            FilterResponse<GetSupplierPaginatedData> flag = supplierService.getSupplierPaginatedData(requestParam);
            if(flag.getData().isEmpty()) {
                result= new GeneralResponse<>(flag, ConstantFile.Supplier_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(flag, ConstantFile.Supplier_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @PostMapping("/supplier/rates")
    public ResponseEntity<GeneralResponse<Boolean,Object>> addSupplierRates(@RequestBody AddSupplierRateRequest addSupplierRateRequest){

        GeneralResponse<Boolean,Object> result;
        try{
            Boolean flag = supplierService.addSupplierRates(addSupplierRateRequest);
            if(flag) {
                result= new GeneralResponse<>(true, ConstantFile.Supplier_Added, true, System.currentTimeMillis(), HttpStatus.OK,addSupplierRateRequest);
            } else {
                result= new GeneralResponse<>(false, ConstantFile.Supplier_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,addSupplierRateRequest);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,addSupplierRateRequest);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/supplier/rates/exist")
    public ResponseEntity<GeneralResponse<Boolean,Object>> getSupplierRateExist(@RequestParam(name = "name") String name,@RequestParam(name = "id") Long id){

        GeneralResponse<Boolean,Object> result;
        try{
            Boolean flag = supplierService.supplierRateExist(name,id);
            if(flag) {
                result= new GeneralResponse<>(true, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(false, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();

            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/supplier/rate/{id}")
    public ResponseEntity<GeneralResponse<SupplierResponse,Object>> getSupplierAlongWithRates(@PathVariable("id") Long id){
        GeneralResponse<SupplierResponse,Object> result;
        try{
            if(id == null){
                throw new Exception(ConstantFile.Null_Record_Passed);//result= new GeneralResponse<>(null, "Id cannot be null", false, System.currentTimeMillis(), HttpStatus.OK);
            }

            SupplierResponse obj =  supplierService.getSupplier(id);
            if(obj != null){
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/supplier/GetItemWithRateBy/{id}")
    public ResponseEntity<GeneralResponse<List<RateAndItem>,Object>> getSupplierItemWithRateById(@PathVariable("id") Long id){
        GeneralResponse<List<RateAndItem>,Object> result;

        try{
            if(id == null){
                throw new Exception(ConstantFile.Null_Record_Passed);//result= new GeneralResponse<>(null, "Id cannot be null", false, System.currentTimeMillis(), HttpStatus.OK);
            }

            List<RateAndItem> item =  supplierService.getSupplierItemAndRate(id);
            if(item != null){
                result= new GeneralResponse<>(item, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/supplier/rates/all")
    public ResponseEntity<GeneralResponse<List<GetAllSupplierRatesResponse>,Object>> getAllSupplierRates(){
        GeneralResponse<List<GetAllSupplierRatesResponse>,Object> result;
        try{
            List<GetAllSupplierRatesResponse> obj = supplierService.getAllRates();
            if(!obj.isEmpty()){
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/supplier/rates/byQualityNameId")
    public ResponseEntity<GeneralResponse<List<SupplierRateResponse>,Object>> getAllSupplierRatesByQualityNameId(@RequestParam("qualityNameId") Long qualityNameId,@RequestParam("type") String type){
        GeneralResponse<List<SupplierRateResponse>,Object> result;
        try{
            if(qualityNameId==null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            List<SupplierRateResponse> obj = supplierService.getAllRatesByQualityNameId(qualityNameId,type);
            if(!obj.isEmpty()){
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/supplier/getItemWithSupplierName/all")
    public ResponseEntity<GeneralResponse<List<GetItemWithSupplier>,Object>> getAllItemWithSupplierName(){
        GeneralResponse<List<GetItemWithSupplier>,Object> result;
        try{
            List<GetItemWithSupplier> obj = supplierService.getAllItemWithSupplierName();
            if(obj != null){
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/supplier/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<Supplier>,Object>> getAllSupplier(@PathVariable(value = "id") Long id,@PathVariable( value = "getBy") String getBy){
        GeneralResponse<List<Supplier>,Object> result;
        try{
            switch (getBy) {
                case "own":
                    List<Supplier> obj = supplierService.getAllSupplier(getBy, id);
                    if(!obj.isEmpty()){
                        result= new GeneralResponse<>(obj, ConstantFile.Supplier_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    } else
                        result= new GeneralResponse<>(null, ConstantFile.Supplier_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                    break;
                case "group":
                    List<Supplier> obj1 = supplierService.getAllSupplier(getBy, id);

                    if(!obj1.isEmpty()){
                        result= new GeneralResponse<>(obj1, ConstantFile.Supplier_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    } else {
                        result= new GeneralResponse<>(null, ConstantFile.Supplier_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    }
                    break;

                case "all":
                    List<Supplier> obj2 = supplierService.getAllSupplier(null, null);
                    if(!obj2.isEmpty()){
                        result= new GeneralResponse<>(obj2, ConstantFile.Supplier_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    } else {
                        result= new GeneralResponse<>(null, ConstantFile.Supplier_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    }

                    break;
                default:
                    result= new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());


            }
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }

        logService.saveLog(result,request,debugAll);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
    @GetMapping("/supplier/all")
    public ResponseEntity<GeneralResponse<List<GetAllSupplierWithName>,Object>> getAllSupplierName(){
        GeneralResponse<List<GetAllSupplierWithName>,Object> result;
        try{

            List<GetAllSupplierWithName> object =supplierService.getAllSupplierName();

            if(!object.isEmpty()) {
                result = new GeneralResponse<>(object, ConstantFile.Supplier_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
            }else
            {
                result = new GeneralResponse<>(object, ConstantFile.Supplier_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/supplier")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateSupplier(@RequestBody AddSupplier addSupplier){
        GeneralResponse<Boolean,Object> result;
        try{
            Boolean flag = supplierService.updateSupplier(addSupplier);
            if (flag) {
                result= new GeneralResponse<>(null, ConstantFile.Supplier_Updated, true, System.currentTimeMillis(), HttpStatus.OK,addSupplier);
            } else {
                result= new GeneralResponse<>(null, ConstantFile.Supplier_Invalid_Data, false, System.currentTimeMillis(), HttpStatus.OK,addSupplier);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,addSupplier);
            logService.saveLog(result,request,debugAll);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/supplier/rates")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateSupplier(@RequestBody UpdateSupplierRatesRequest updateSupplierRequest){

        GeneralResponse<Boolean,Object> result;
        try{
            supplierService.updateSupplierRatesWithValidation(updateSupplierRequest);

                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Updated, true, System.currentTimeMillis(), HttpStatus.OK,updateSupplierRequest);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,updateSupplierRequest);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    //get color item of supplier
    @GetMapping("/supplier/rate/colorItemsById/{supplierId}")
    public ResponseEntity<GeneralResponse<List<SupplierRate>,Object>> getAllColorItemBySupplierId(@PathVariable(name = "supplierId") Long supplierId){


        GeneralResponse<List<SupplierRate>,Object> result;
        try{
            List<SupplierRate> obj = supplierService.getAllColorItemBySupplierId(supplierId);
            if(obj != null){
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get color item of supplier
    @GetMapping("/supplier/rate/chemicalItemsById/{supplierId}")
    public ResponseEntity<GeneralResponse<List<SupplierRate>,Object>> getAllChemicalItemBySupplierId(@PathVariable(name ="supplierId") Long supplierId){

        GeneralResponse<List<SupplierRate>,Object> result;
        try{
            List<SupplierRate> obj = supplierService.getAllChemicalItemBySupplierId(supplierId);
            if(obj != null){
                result= new GeneralResponse<>(obj, ConstantFile.SupplierRate_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result= new GeneralResponse<>(null, ConstantFile.SupplierRate_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
