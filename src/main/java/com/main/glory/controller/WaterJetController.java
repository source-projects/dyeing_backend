package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.user.Request.UserAddRequest;
import com.main.glory.model.waterJet.WaterJet;
import com.main.glory.servicesImpl.WaterJetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WaterJetController extends ControllerConfig {

    @Autowired
    WaterJetServiceImpl waterJetService;

    @PostMapping("/waterJet")
    public GeneralResponse<Boolean> saveWaterJet(@RequestBody WaterJet waterJetData) throws Exception{

        try{
            waterJetService.saveWaterJet(waterJetData);
            return new GeneralResponse<>(true,"Water Jet created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/waterJet/{id}")
    public GeneralResponse<WaterJet> getWaterJet(@PathVariable(name="id") Long id) throws Exception{

        try{
            WaterJet data = waterJetService.getWaterJetById(id);
            return new GeneralResponse<>(data,"Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/waterJet/delete/{id}")
    public GeneralResponse<Boolean> deleteWaterJet(@PathVariable(name="id") Long id) throws Exception{

        try{
            waterJetService.deleteWaterJet(id);
            return new GeneralResponse<>(true,"Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping("/waterJet/delete/{id}")
    public GeneralResponse<Boolean> updateWaterJet(@RequestBody WaterJet waterJet) throws Exception{

        try{
            waterJetService.updateWaterJet(waterJet);
            return new GeneralResponse<>(true,"Data updated successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/waterJet/all")
    public GeneralResponse<List<WaterJet>> GetAllWaterJet() throws Exception{

        try{
            List<WaterJet> waterJetList = waterJetService.getAllWaterJet();
            return new GeneralResponse<>(waterJetList,"Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
}
