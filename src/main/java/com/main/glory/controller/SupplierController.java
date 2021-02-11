package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SupplierController extends ControllerConfig {

    @Autowired
    SupplierServiceImpl supplierService;

    @PostMapping("/supplier")
    public GeneralResponse<Boolean> addSupplier(@RequestBody Supplier supplier){
        try{
            Boolean flag = supplierService.addSupplier(supplier);
            if(flag) {
                return new GeneralResponse<Boolean>(true, "Supplier added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<Boolean>(false, "Invalid Data Sent", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/supplier/rates")
    public GeneralResponse<Boolean> addSupplierRates(@RequestBody AddSupplierRateRequest addSupplierRateRequest){
        try{
            Boolean flag = supplierService.addSupplierRates(addSupplierRateRequest);
            if(flag) {
                return new GeneralResponse<Boolean>(true, "Supplier rates added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<Boolean>(false, "Invalid Data Sent", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/supplier/rate/{id}")
    public GeneralResponse<Object> getSupplierAlongWithRates(@PathVariable("id") Long id){
        try{
            if(id == null){
                return new GeneralResponse<>(null, "Id cannot be null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }

            Object obj =  supplierService.getSupplier(id);
            if(obj != null){
                return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/supplier/GetItemWithRateBy/{id}")
    public GeneralResponse<List<RateAndItem>> getSupplierItemWithRateById(@PathVariable("id") Long id){
        try{
            if(id == null){
                return new GeneralResponse<>(null, "Id cannot be null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }

            List<RateAndItem> item =  supplierService.getSupplierItemAndRate(id);
            if(item != null){
                return new GeneralResponse<>(item, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/supplier/rates/all")
    public GeneralResponse<Object> getAllSupplierRates(){
        try{
            Object obj = supplierService.getAllRates();
            if(obj != null){
                return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/supplier/getItemWithSupplierName/all")
    public GeneralResponse<List<GetItemWithSupplier>> getAllItemWithSupplierName(){
        try{
            List<GetItemWithSupplier> obj = supplierService.getAllItemWithSupplierName();
            if(obj != null){
                return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/supplier/all/{getBy}/{id}")
    public GeneralResponse<List> getAllSupplier(@PathVariable(value = "id") Long id,@PathVariable( value = "getBy") String getBy){
        try{
            switch (getBy) {
                case "own":
                    List obj = supplierService.getAllSupplier(getBy, id);
                    if(!obj.isEmpty()){
                        return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    } else
                        return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

                case "group":
                    List obj1 = supplierService.getAllSupplier(getBy, id);

                    if(!obj1.isEmpty()){
                        return new GeneralResponse<>(obj1, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    } else {
                        return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }

                case "all":
                    List obj2 = supplierService.getAllSupplier(null, null);
                    if(!obj2.isEmpty()){
                        return new GeneralResponse<>(obj2, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    } else {
                        return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                    }

                default:
                    return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/supplier/all")
    public GeneralResponse<List<GetAllSupplierWithName>> getAllSupplierName(){
        try{

            List<GetAllSupplierWithName> object =supplierService.getAllSupplierName();

            return new GeneralResponse<>(object, "Data found", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/supplier")
    public GeneralResponse<Boolean> updateSupplier(@RequestBody UpdateSupplierRequest updateSupplierRequest){
        try{
            Boolean flag = supplierService.updateSupplier(updateSupplierRequest);
            if (flag) {
                return new GeneralResponse<>(null, "Supplier Details Updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "Send Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/supplier/rates")
    public GeneralResponse<Boolean> updateSupplier(@RequestBody UpdateSupplierRatesRequest updateSupplierRequest){
        try{
            Boolean flag = supplierService.updateSupplierRates(updateSupplierRequest);
            if (flag) {
                return new GeneralResponse<>(null, "Supplier Details Updated Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "Send Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
