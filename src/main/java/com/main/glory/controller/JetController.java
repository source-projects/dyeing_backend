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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class JetController extends ControllerConfig {

    @Autowired
    JetServiceImpl jetService;




    @PostMapping(value="/jet/addJetData")
    public ResponseEntity<GeneralResponse<Boolean>> saveJetData(@RequestBody List<AddJetData> jetData) throws Exception {

        GeneralResponse<Boolean> result;
        if(jetData==null)
        {
            result = new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            jetService.saveJetData(jetData);
            result = new GeneralResponse<Boolean>(null, "Jet Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/jet/updateJetData")
    public ResponseEntity<GeneralResponse<Boolean>> updateJetData(@RequestBody UpdateJetData jetDataToUpdate) throws Exception {

        GeneralResponse<Boolean> result;
        if(jetDataToUpdate==null)
        {
            result = new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            jetService.updateJetData(jetDataToUpdate);
            result = new GeneralResponse<Boolean>(null, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/jet/updateJetData/productionStatus/")
    public ResponseEntity<GeneralResponse<Boolean>> updateJetData(@RequestBody ChangeStatus jetDataToUpdate,@RequestHeader Map<String, String> headers) throws Exception {

        GeneralResponse<Boolean> result;
        if(jetDataToUpdate==null)
        {
            result = new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            jetService.updateProductionStatus(jetDataToUpdate,headers.get("id"));
            result = new GeneralResponse<Boolean>(null, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value="/jet/getJetData/{id}")
    public ResponseEntity<GeneralResponse<List<GetJetData>>> getJetData(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<List<GetJetData>> result;
        if(id==null)
        {
            result = new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            List<GetJetData> jetDataList = jetService.getJetData(id);
            result = new GeneralResponse<>(jetDataList, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/jet/getAllJetDetail")
    public ResponseEntity<GeneralResponse<List<GetAllJetMast>>> getAllJetData()  throws Exception {

        GeneralResponse<List<GetAllJetMast>> result;
        boolean flag;
        try {

            List<GetAllJetMast> jetMastList = jetService.getAllJetData();
            result = new GeneralResponse<>(jetMastList, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get the jet slip
    @GetMapping(value="/jet/getJetSlipData/{jetId}/{productionId}")
    public ResponseEntity<GeneralResponse<GetJetSlip>> getJetSlipData(@PathVariable(name = "jetId") Long jetId , @PathVariable(name = "productionId") Long productionId)  throws Exception {
        GeneralResponse<GetJetSlip> result;
        boolean flag;
        try {
            GetJetSlip slipData = jetService.getJetSlipData(jetId,productionId);
            result = new GeneralResponse<>(slipData, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //remove the production from the jet
    @DeleteMapping(value="/jet/delete/removeProductionFromJet/{jetId}/{productionId}")
    public ResponseEntity<GeneralResponse<Boolean>> removeProductionFromJet(@PathVariable(name = "jetId") Long jetId , @PathVariable(name = "productionId") Long productionId)  throws Exception {
        GeneralResponse<Boolean> result;
        boolean flag;
        try {
            jetService.removeProductionFromJet(jetId,productionId);
            result = new GeneralResponse<>(true, "production removed successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @GetMapping(value="/jet/getJetDataWithInQueueProdution/byJetId/{id}")
    public ResponseEntity<GeneralResponse<List<GetJetData>>> getJetDataWithInQueueProdution(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<List<GetJetData>> result;
        if(id==null)
        {
             result=  new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }


        boolean flag;
        try {

            List<GetJetData> jetDataList = jetService.getJetDataWithInQueueProdution(id);
            result =  new GeneralResponse<>(jetDataList, "Jet Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/jet/getJet/allStatusList")
    public ResponseEntity<GeneralResponse<List<GetStatus>>> getJetStatusList() throws Exception {

        GeneralResponse<List<GetStatus>> result;
        boolean flag;
        try {

            List<GetStatus> jetDataList = jetService.getJetStatusList();
            result = new GeneralResponse<>(jetDataList, "Jet status list fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value="/jet/deleteJetDataByProductionId/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteJetDataByProductionId(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<Boolean> result;
        if(id==null) {
            result = new GeneralResponse<>(null, "production info is null", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        try {

            Boolean flag = jetService.deleteJetDataByProductionId(id);
            if(flag==true)
            result = new GeneralResponse<>(true, "Jet Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(false, "unable to delete the prouduction", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value="/jet/deleteJetMastByJetId/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteJetMastByJetId(@PathVariable(name = "id") Long id) throws Exception {
        GeneralResponse<Boolean> result;
        if(id==null)
        {
            result = new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        try {

            Boolean flag = jetService.deleteJetMastByJetId(id);
            if(flag==true)
                result = new GeneralResponse<>(true, "Jet Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result = new GeneralResponse<>(false, "unable to delete the prouduction", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
}
