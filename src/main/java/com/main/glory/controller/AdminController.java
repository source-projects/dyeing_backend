package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.admin.ApprovedBy;
import com.main.glory.model.admin.Company;
import com.main.glory.model.jet.request.AddJet;
import com.main.glory.servicesImpl.AdminServciceImpl;
import com.main.glory.servicesImpl.JetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController extends ControllerConfig {

    @Autowired
    JetServiceImpl jetService;

    @Autowired
    AdminServciceImpl adminServcice;


    @PostMapping(value="/admin/jet/addJet")
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



    @PostMapping(value="/admin/add/company/")
    public GeneralResponse<Boolean> saveCompany(Company c) throws Exception {

        GeneralResponse<Boolean> result;
        if(c.getName()==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            adminServcice.saveCompanyName(c);
            result= new GeneralResponse<Boolean>(null, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }
    @DeleteMapping(value="/admin/delete/companyBy/{id}")
    public GeneralResponse<Boolean> deleteCompany(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;
        if(id==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            flag = adminServcice.deleteCompanyById(id);
            if(flag)
            result= new GeneralResponse<Boolean>(null, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<Boolean>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }


    @PostMapping(value="/admin/add/approvedBy/")
    public GeneralResponse<Boolean> saveApprovedBy(@RequestBody ApprovedBy data) throws Exception {

        GeneralResponse<Boolean> result;
        if(data==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

        boolean flag;
        try {

            adminServcice.saveApprovedBy(data);
            result= new GeneralResponse<Boolean>(null, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }


    @GetMapping(value="/admin/get/approvedBy")
    public GeneralResponse<List<ApprovedBy>> getAllApproved() throws Exception {

        GeneralResponse<List<ApprovedBy>> result;

        boolean flag;
        try {

            List<ApprovedBy> list = adminServcice.getApprovedByList();
            if(list.isEmpty())
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.CREATED);
            else
            result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @GetMapping(value="/admin/get/allJet")
    public GeneralResponse<List<AddJet>> getAllJet() throws Exception {

        GeneralResponse<List<AddJet>> result;

        boolean flag;
        try {

            List<AddJet> list = jetService.getAllJet();
            if(list.isEmpty())
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }


    @GetMapping(value="/admin/get/allCompany")
    public GeneralResponse<List<Company>> getAllCompany() throws Exception {

        GeneralResponse<List<Company>> result;

        boolean flag;
        try {

            List<Company> list = adminServcice.getAllCompany();
            if(list.isEmpty())
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @DeleteMapping(value="/admin/delete/jet/{id}")
    public GeneralResponse<Boolean> deleteJetById(@PathVariable(name = "id") Long id) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

            Boolean list = jetService.deleteJetMastByJetId(id);
            if(list==false)
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                result= new GeneralResponse<>(list, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @DeleteMapping(value="/admin/delete/approved/{id}")
    public GeneralResponse<Boolean> deleteApprovedById(@PathVariable(name = "id") Long id) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

            Boolean list = adminServcice.deleteApprovedById(id);
            if(list==false)
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                result= new GeneralResponse<>(list, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }



}
