package com.main.glory.controller;


import com.main.glory.model.GeneralResponse;
import com.main.glory.model.designation.Designation;
import com.main.glory.model.user.UserData;
import com.main.glory.services.DesignationServiceInterface;
import com.main.glory.servicesImpl.DesignationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DesignationController {

    @Autowired
    private DesignationServiceImpl designationService;

    @PostMapping("/designation")
    public GeneralResponse<Boolean> saveDesignation(@RequestBody Designation designationData) throws Exception{

        try{
            int flag = designationService.createDesignation(designationData);
            if(flag==1){
                return new GeneralResponse<>(true,"Designation created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(true,"Designation not created successfully", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/designation")
    public GeneralResponse<List<Designation>> getDesignation() throws Exception{

        try{
            List<Designation> flag = designationService.getDesignation();
            if(flag!=null){
                return new GeneralResponse<>(flag,"Designation Fetch successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(flag,"Designation not Fetch successfully", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/designation/{id}")
    public GeneralResponse<Optional<Designation>> getDesignationById(@PathVariable(value = "id") Long id) throws Exception{

        try{
            Optional<Designation> flag = designationService.getDesignationById(id);
            if(flag!=null){
                return new GeneralResponse<>(flag,"Designation Fetch successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(flag,"Designation not Fetch successfully", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
