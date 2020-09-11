package com.main.glory.controller;

import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.servicesImpl.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SupplierController {
    @Autowired
    SupplierServiceImpl supplierService;

    @PostMapping("/add-supplier")
    public Boolean addSupplier(@RequestBody Supplier supplier){
        return supplierService.addSupplier(supplier);
    }

    @PostMapping("/add-supplier-rates")
    public Boolean addSupplierRates(@RequestBody AddSupplierRateRequest addSupplierRateRequest){
        return supplierService.addSupplierRates(addSupplierRateRequest);
    }

    @GetMapping("/supplier/{id}")
    public Object getSupplierAlongWithRates(@PathVariable("id") Long id){
        return supplierService.getSupplier(id);
    }

    @PutMapping("/supplier")
    public Boolean updateSupplier(@RequestBody UpdateSupplierRequest updateSupplierRequest){
        return supplierService.updateSupplier(updateSupplierRequest);
    }
}
