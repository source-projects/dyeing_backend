package com.main.glory.controller;

import java.util.List;

import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.BatchGrDetail;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.Party;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.main.glory.model.Fabric;
import com.main.glory.servicesImpl.FabricsServiceImpl;

@RestController
@RequestMapping("/api")
public class FabricsController extends ControllerConfig {

    @Autowired
    private FabricsServiceImpl fabricsServiceImpl;

    @PostMapping("/add-fab-stock")
    public GeneralResponse<Boolean> addFabricIn(@RequestBody Fabric fabrics) throws Exception {
        int flag = fabricsServiceImpl.saveFabrics(fabrics);
        if (flag != 1) {
            return new GeneralResponse<Boolean>(null, "Please Enter Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        } else
            return new GeneralResponse<Boolean>(null, "FabStock Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
    }

    @GetMapping("/get-all-fablist")
    public GeneralResponse<List<MasterLookUpWithRecord>> getFabListData() {
        try {
            var x = fabricsServiceImpl.getFabStockMasterListRecord();
            return new GeneralResponse<List<MasterLookUpWithRecord>>(x, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new GeneralResponse<List<MasterLookUpWithRecord>>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get-fab-stock-by-id/{id}")
    public GeneralResponse<Fabric> getFabStockDataById(@PathVariable(value = "id") Long id) {

        if (id != null) {
            var fabData = fabricsServiceImpl.getFabRecordById(id);
            if (fabData != null) {
                return new GeneralResponse<Fabric>(fabData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
            } else
                return new GeneralResponse<Fabric>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        } else
            return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

    }


    @PostMapping("/update-stock")
    public GeneralResponse<Boolean> updateFabricIn(@RequestBody Fabric fabrics) throws Exception {
        if (fabrics != null) {
            boolean flag = fabricsServiceImpl.updateFabricsDetails(fabrics);
            if (flag) {
                return new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GeneralResponse<Boolean>(false, "Null Party Object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/delete-stock/{id}")
    public GeneralResponse<Boolean> deleteFabDetailsByID(@PathVariable(value = "id") Long id) {
        if (id != null) {
            boolean flag = fabricsServiceImpl.deleteFabricsById(id);
            if (flag) {
                return new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }
}
