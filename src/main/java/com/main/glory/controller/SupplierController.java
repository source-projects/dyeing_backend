package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.model.supplier.responce.GetAllSupplierWithName;
import com.main.glory.model.supplier.responce.GetItemWithSupplier;
import com.main.glory.model.supplier.responce.GetSupplierWithRateAndItem;
import com.main.glory.model.supplier.responce.RateAndItem;
import com.main.glory.servicesImpl.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SupplierController extends ControllerConfig {

    @Autowired
    SupplierServiceImpl supplierService;

    @GetMapping("/isSupplierNameExists/{name}/{id}")
    public GeneralResponse<Boolean> isUniqueSupplierName(@PathVariable("name") String name, @PathVariable("id") Long id){
        try{
            Boolean isPresent = supplierService.isUniqueName(id, name);
            return new GeneralResponse<Boolean>(isPresent, "Supplier name exists:"+isPresent, true, System.currentTimeMillis(), HttpStatus.OK);
        }catch (Exception e){
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/supplier")
    public ResponseEntity<GeneralResponse<Boolean>> addSupplier(@RequestBody Supplier supplier){
        GeneralResponse<Boolean> result;
        try{
            Boolean flag = supplierService.addSupplier(supplier);
            if(flag) {
                result= new GeneralResponse<Boolean>(true, "Supplier added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<Boolean>(false, "Invalid Data Sent", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/supplier/rates")
    public ResponseEntity<GeneralResponse<Boolean>> addSupplierRates(@RequestBody AddSupplierRateRequest addSupplierRateRequest){

        GeneralResponse<Boolean> result;
        try{
            Boolean flag = supplierService.addSupplierRates(addSupplierRateRequest);
            if(flag) {
                result= new GeneralResponse<Boolean>(true, "Supplier rates added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<Boolean>(false, "Invalid Data Sent", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/supplier/rate/{id}")
    public ResponseEntity<GeneralResponse<Object>> getSupplierAlongWithRates(@PathVariable("id") Long id){
        GeneralResponse<Object> result;
        try{
            if(id == null){
                result= new GeneralResponse<>(null, "Id cannot be null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }

            Object obj =  supplierService.getSupplier(id);
            if(obj != null){
                result= new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/supplier/GetItemWithRateBy/{id}")
    public ResponseEntity<GeneralResponse<List<RateAndItem>>> getSupplierItemWithRateById(@PathVariable("id") Long id){
        GeneralResponse<List<RateAndItem>> result;

        try{
            if(id == null){
                result= new GeneralResponse<>(null, "Id cannot be null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }

            List<RateAndItem> item =  supplierService.getSupplierItemAndRate(id);
            if(item != null){
                result= new GeneralResponse<>(item, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/supplier/rates/all")
    public ResponseEntity<GeneralResponse<Object>> getAllSupplierRates(){
        GeneralResponse<Object> result;
        try{
            Object obj = supplierService.getAllRates();
            if(obj != null){
                result= new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/supplier/getItemWithSupplierName/all")
    public ResponseEntity<GeneralResponse<List<GetItemWithSupplier>>> getAllItemWithSupplierName(){
        GeneralResponse<List<GetItemWithSupplier>> result;
        try{
            List<GetItemWithSupplier> obj = supplierService.getAllItemWithSupplierName();
            if(obj != null){
                result= new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/supplier/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List>> getAllSupplier(@PathVariable(value = "id") Long id,@PathVariable( value = "getBy") String getBy){
        GeneralResponse<List> result;
        try{
            switch (getBy) {
                case "own":
                    List obj = supplierService.getAllSupplier(getBy, id);
                    if(!obj.isEmpty()){
                        result= new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    } else
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

                    break;
                case "group":
                    List obj1 = supplierService.getAllSupplier(getBy, id);

                    if(!obj1.isEmpty()){
                        result= new GeneralResponse<>(obj1, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    } else {
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }
                    break;

                case "all":
                    List obj2 = supplierService.getAllSupplier(null, null);
                    if(!obj2.isEmpty()){
                        result= new GeneralResponse<>(obj2, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    } else {
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }

                    break;
                default:
                    result= new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
    @GetMapping("/supplier/all")
    public ResponseEntity<GeneralResponse<List<GetAllSupplierWithName>>> getAllSupplierName(){
        GeneralResponse<List<GetAllSupplierWithName>> result;
        try{

            List<GetAllSupplierWithName> object =supplierService.getAllSupplierName();

            result= new GeneralResponse<>(object, "Data found", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/supplier")
    public ResponseEntity<GeneralResponse<Boolean>> updateSupplier(@RequestBody UpdateSupplierRequest updateSupplierRequest){
        GeneralResponse<Boolean> result;
        try{
            Boolean flag = supplierService.updateSupplier(updateSupplierRequest);
            if (flag) {
                result= new GeneralResponse<>(null, "Supplier Details Updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "Send Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/supplier/rates")
    public ResponseEntity<GeneralResponse<Boolean>> updateSupplier(@RequestBody UpdateSupplierRatesRequest updateSupplierRequest){

        GeneralResponse<Boolean> result;
        try{
            supplierService.updateSupplierRatesWithValidation(updateSupplierRequest);

                result= new GeneralResponse<>(null, "Supplier Details Updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    //get color item of supplier
    @GetMapping("/supplier/rate/colorItemsById/{supplierId}")
    public ResponseEntity<GeneralResponse<List<SupplierRate>>> getAllColorItemBySupplierId(@PathVariable(name = "supplierId") Long supplierId){


        GeneralResponse<List<SupplierRate>> result;
        try{
            List<SupplierRate> obj = supplierService.getAllColorItemBySupplierId(supplierId);
            if(obj != null){
                result= new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get color item of supplier
    @GetMapping("/supplier/rate/chemicalItemsById/{supplierId}")
    public ResponseEntity<GeneralResponse<List<SupplierRate>>> getAllChemicalItemBySupplierId(@PathVariable(name ="supplierId") Long supplierId){

        GeneralResponse<List<SupplierRate>> result;
        try{
            List<SupplierRate> obj = supplierService.getAllChemicalItemBySupplierId(supplierId);
            if(obj != null){
                result= new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            result= new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
