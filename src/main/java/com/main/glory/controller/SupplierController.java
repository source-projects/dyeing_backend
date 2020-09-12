package com.main.glory.controller;

import com.main.glory.model.GeneralResponse;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.servicesImpl.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SupplierController {
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

    @GetMapping("/supplier/all")
    public GeneralResponse<Object> getAllSupplier(){
        try{
            Object obj = supplierService.getAllSupplier();
            if(obj != null){
                return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
