package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.jet.request.AddJet;
import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.request.ChangeStatus;
import com.main.glory.model.jet.request.UpdateJetData;
import com.main.glory.model.jet.responce.*;
import com.main.glory.servicesImpl.JetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JetController extends ControllerConfig {

    @Autowired
    JetServiceImpl jetService;

    @PostMapping(value="/jet/addJet")
    public GeneralResponse<Boolean> saveJet(@RequestBody AddJet jetMast) throws Exception {
        if(jetMast==null)
        {
            return new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            jetService.saveJet(jetMast);
            return new GeneralResponse<Boolean>(null, "Jet Data added successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @PostMapping(value="/jet/addJetData")
    public GeneralResponse<Boolean> saveJetData(@RequestBody List<AddJetData> jetData) throws Exception {
        if(jetData==null)
        {
            return new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            jetService.saveJetData(jetData);
            return new GeneralResponse<Boolean>(null, "Jet Data added successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/jet/updateJetData")
    public GeneralResponse<Boolean> updateJetData(@RequestBody UpdateJetData jetDataToUpdate) throws Exception {
        if(jetDataToUpdate==null)
        {
            return new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            jetService.updateJetData(jetDataToUpdate);
            return new GeneralResponse<Boolean>(null, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/jet/updateJetData/productionStatus/")
    public GeneralResponse<Boolean> updateJetData(@RequestBody ChangeStatus jetDataToUpdate) throws Exception {
        if(jetDataToUpdate==null)
        {
            return new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            jetService.updateProductionStatus(jetDataToUpdate);
            return new GeneralResponse<Boolean>(null, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value="/jet/getJetData/{id}")
    public GeneralResponse<List<GetJetData>> getJetData(@PathVariable(name = "id") Long id) throws Exception {
        if(id==null)
        {
            return new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            List<GetJetData> jetDataList = jetService.getJetData(id);
            return new GeneralResponse<>(jetDataList, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/jet/getAllJetDetail")
    public GeneralResponse<List<GetAllJetMast>> getAllJetData()  throws Exception {

        boolean flag;
        try {

            List<GetAllJetMast> jetMastList = jetService.getAllJetData();
            return new GeneralResponse<>(jetMastList, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get the jet slip
    @GetMapping(value="/jet/getJetSlipData/{jetId}/{productionId}")
    public GeneralResponse<GetJetSlip> getJetSlipData(@PathVariable(name = "jetId") Long jetId , @PathVariable(name = "productionId") Long productionId)  throws Exception {
        GeneralResponse<GetJetSlip> result;
        boolean flag;
        try {
            GetJetSlip slipData = jetService.getJetSlipData(jetId,productionId);
            result = new GeneralResponse<>(slipData, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }


    @GetMapping(value="/jet/getJetDataWithInQueueProdution/byJetId/{id}")
    public GeneralResponse<List<GetJetData>> getJetDataWithInQueueProdution(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<List<GetJetData>> result;
        if(id==null)
        {
             result=  new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }


        boolean flag;
        try {

            List<GetJetData> jetDataList = jetService.getJetDataWithInQueueProdution(id);
            result =  new GeneralResponse<>(jetDataList, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @GetMapping(value="/jet/getJet/allStatusList")
    public GeneralResponse<List<GetStatus>> getJetStatusList() throws Exception {

        boolean flag;
        try {

            List<GetStatus> jetDataList = jetService.getJetStatusList();
            return new GeneralResponse<>(jetDataList, "Jet status list fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/jet/deleteJetDataByProductionId/{id}")
    public GeneralResponse<Boolean> deleteJetDataByProductionId(@PathVariable(name = "id") Long id) throws Exception {
        if(id==null)
        {
            return new GeneralResponse<>(null, "production info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        try {


            Boolean flag = jetService.deleteJetDataByProductionId(id);
            if(flag==true)
            return new GeneralResponse<>(true, "Jet Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false, "unable to delete the prouduction", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/jet/deleteJetMastByJetId/{id}")
    public GeneralResponse<Boolean> deleteJetMastByJetId(@PathVariable(name = "id") Long id) throws Exception {
        if(id==null)
        {
            return new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        try {

            Boolean flag = jetService.deleteJetMastByJetId(id);
            if(flag==true)
                return new GeneralResponse<>(true, "Jet Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false, "unable to delete the prouduction", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
}
