package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
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
public class DesignationController extends ControllerConfig {

    @Autowired
    private DesignationServiceImpl designationService;

    @PostMapping("/user/designation")
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
    @GetMapping(value="/user/designation/deleteTable/{id}")
    public GeneralResponse<Boolean> isDesignationDeletable(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;

        try {

            Boolean flag = designationService.getDesignationIsDelatable(id);
            if(flag)
                result= new GeneralResponse<>(flag, "data is deletable", false, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                result= new GeneralResponse<>(flag, "data is not deletable", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }


    @GetMapping("/user/designation")
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
    @DeleteMapping("/user/designation/{id}")
    public GeneralResponse<Boolean> deleteDesignationById(@PathVariable(name = "id") Long id) throws Exception{

        try{
            Boolean flag = designationService.deleteDesignationById(id);
            if(flag){
                return new GeneralResponse<>(flag,"Designation deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(flag,"Designation not found ", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/update/designation/")
    public GeneralResponse<Boolean> updateDesignation(@RequestBody Designation designation) throws Exception{

        try{

            designationService.updateDesignation(designation);
            return new GeneralResponse<>(true,"Designation updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);


        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
