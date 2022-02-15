package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.servicesImpl.WaterJetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WaterJetController extends ControllerConfig {

    @Autowired
    WaterJetServiceImpl waterJetService;
/*
    @PostMapping("/waterJet")
    public ResponseEntity<GeneralResponse<Boolean>> saveWaterJet(@RequestBody WaterJet waterJetData) throws Exception{

        GeneralResponse<Boolean> result;
        try{
            waterJetService.saveWaterJet(waterJetData);
            result = new GeneralResponse<>(true,"Water Jet created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/waterJet/{id}")
    public ResponseEntity<GeneralResponse<WaterJet>> getWaterJet(@PathVariable(name="id") Long id) throws Exception{

        GeneralResponse<WaterJet> result;

        try{
            WaterJet data = waterJetService.getWaterJetById(id);
            result = new GeneralResponse<>(data,"Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
    @DeleteMapping("/waterJet/delete/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteWaterJet(@PathVariable(name="id") Long id) throws Exception{

        GeneralResponse<Boolean> result;
        try{
            waterJetService.deleteWaterJet(id);
            result = new GeneralResponse<>(true,"Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
    @PutMapping("/waterJet/update/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> updateWaterJet(@RequestBody WaterJet waterJet) throws Exception{

        GeneralResponse<Boolean> result;
        try{
            waterJetService.updateWaterJet(waterJet);
            result = new GeneralResponse<>(true,"Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
    @GetMapping("/waterJet/all")
    public ResponseEntity<GeneralResponse<List<WaterJet>>> GetAllWaterJet() throws Exception{

        GeneralResponse<List<WaterJet>> result;
        try{
            List<WaterJet> waterJetList = waterJetService.getAllWaterJet();
            result = new GeneralResponse<>(waterJetList,"Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }*/
}
