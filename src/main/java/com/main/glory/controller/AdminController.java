package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.admin.*;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.request.AddJet;
import com.main.glory.model.quality.QualityName;
import com.main.glory.servicesImpl.AdminServciceImpl;
import com.main.glory.servicesImpl.JetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            jetService.saveJet(jetMast);
            return new GeneralResponse<Boolean>(null, "Jet Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }
    @PostMapping(value="/admin/quality/add/qualityName/")
    public GeneralResponse<Boolean> saveQualityName(@RequestBody QualityName qualityName) throws Exception {
        if(qualityName==null)
        {
            return new GeneralResponse<Boolean>(false, "qualityName info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.saveQualityName(qualityName);
            return new GeneralResponse<Boolean>(null, "Quality Name Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }
    @PutMapping(value="/admin/quality/update/qualityName/")
    public GeneralResponse<Boolean> updateQualityName(@RequestBody QualityName qualityName) throws Exception {
        if(qualityName==null)
        {
            return new GeneralResponse<Boolean>(false, "qualityName info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.updateQualityName(qualityName);
            return new GeneralResponse<Boolean>(null, "Quality Name Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @DeleteMapping(value="/admin/quality/delete/qualityName/{id}")
    public GeneralResponse<Boolean> deleteQualityName(@PathVariable(name = "id") Long id) throws Exception {
        if(id==null)
        {
            return new GeneralResponse<Boolean>(false, "id info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.deleteQualityNameById(id);
            return new GeneralResponse<Boolean>(null, "Quality name deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }


    @PutMapping(value="/admin/jet/updateJet")
    public GeneralResponse<Boolean> updateJet(@RequestBody AddJet jetMast) throws Exception {
        if(jetMast==null)
        {
            return new GeneralResponse<Boolean>(false, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            jetService.updateJet(jetMast);
            return new GeneralResponse<Boolean>(null, "Jet updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }
    @GetMapping(value="/admin/jet/getJetById/{id}")
    public GeneralResponse<JetMast> getJetMast(@PathVariable(name = "id")Long id) throws Exception {
        if(id==null)
        {
            return new GeneralResponse<>(null, "jet info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            JetMast jetMast = jetService.getJetMastById(id);
            return new GeneralResponse<>(jetMast, "Jet data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }


    @PostMapping(value="/admin/add/company/")
    public GeneralResponse<Boolean> saveCompany(@RequestBody Company c) throws Exception {

        GeneralResponse<Boolean> result;
        if(c.getName()==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.saveCompanyName(c);
            result= new GeneralResponse<Boolean>(null, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }
    @GetMapping(value="/admin/get/company/{id}")
    public GeneralResponse<Company> getCompanyById(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Company> result;
        if(id==null)
        {
            result= new GeneralResponse<>(null, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            Company c  = adminServcice.getCompanyById(id);
            result= new GeneralResponse<>(c, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }
    @PutMapping(value="/admin/update/company/")
    public GeneralResponse<Boolean> updateCompany(@RequestBody Company company) throws Exception {

        GeneralResponse<Boolean> result;
        if(company==null)
        {
            result= new GeneralResponse<>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.updateCompany(company);
            result= new GeneralResponse<>(true, " Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }
    @PutMapping(value="/admin/update/department/")
    public GeneralResponse<Boolean> updateDepartMent(@RequestBody Department department) throws Exception {

        GeneralResponse<Boolean> result;
        if(department==null)
        {
            result= new GeneralResponse<>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.updateDepartment(department);
            result= new GeneralResponse<>(true, " Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }



    @PostMapping(value="/admin/add/department/")
    public GeneralResponse<Boolean> saveDepartment(@RequestBody Department c) throws Exception {

        GeneralResponse<Boolean> result;
        if(c.getName()==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.saveDepartment(c);
            result= new GeneralResponse<Boolean>(null, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }
    @DeleteMapping(value="/admin/delete/companyBy/{id}")
    public GeneralResponse<Boolean> deleteCompany(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result=null;
        if(id==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            flag = adminServcice.deleteCompanyById(id);
            if(flag)
            result= new GeneralResponse<Boolean>(null, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }

    @DeleteMapping(value="/admin/delete/department/{id}")
    public GeneralResponse<Boolean> deleteDepartment(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result=null;
        if(id==null)
        {
            result= new GeneralResponse<>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            flag = adminServcice.deleteDepartmentById(id);
            if(flag)
                result= new GeneralResponse<>(null, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }


    @PostMapping(value="/admin/add/approvedBy/")
    public GeneralResponse<Boolean> saveApprovedBy(@RequestBody ApprovedBy data) throws Exception {

        GeneralResponse<Boolean> result;
        if(data==null)
        {
            result= new GeneralResponse<Boolean>(false, " info is null", false, System.currentTimeMillis(), HttpStatus.OK);
        }

        boolean flag;
        try {

            adminServcice.saveApprovedBy(data);
            result= new GeneralResponse<Boolean>(null, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
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
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            else
            result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }
    @PutMapping(value="/admin/update/approvedBy/")
    public GeneralResponse<Boolean> updateApproved(@RequestBody ApprovedBy approvedBy) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

                adminServcice.updateApprovedBy(approvedBy);
                result= new GeneralResponse<>(true, " Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }
    @GetMapping(value="/admin/get/approvedBy/{id}")
    public GeneralResponse<ApprovedBy> getApprovedById(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<ApprovedBy> result;

        boolean flag;
        try {

            if(id==null)
                throw new Exception("data can't be null");

            ApprovedBy data =adminServcice.getApprovedById(id);
            result= new GeneralResponse<>(data, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }

    @GetMapping(value="/admin/get/department")
    public ResponseEntity<GeneralResponse<List<Department>>> getAllDepartment() throws Exception {

        GeneralResponse<List<Department>> result;

        boolean flag;
        try {

            List<Department> list = adminServcice.getAllDepartmentList();
            if(list.isEmpty())
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping(value="/admin/get/department/{id}")
    public ResponseEntity<GeneralResponse<Department>> getDepartmentById(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Department> result;

        boolean flag;
        try {

            Department list = adminServcice.getDepartmentById(id);
            result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    /*@GetMapping(value="/admin/update/department/")
    public GeneralResponse<Boolean> getDepartmentById(@RequestBody Department department) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

            adminServcice.updateDepartment(department);
            result= new GeneralResponse<>(true, " Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }*/
    @GetMapping(value="/admin/get/allJet")
    public ResponseEntity<GeneralResponse<List<AddJet>>> getAllJet() throws Exception {

        GeneralResponse<List<AddJet>> result;

        boolean flag;
        try {

            List<AddJet> list = jetService.getAllJet();
            if(list.isEmpty())
                result= new GeneralResponse<>(list, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/jet/deleteTable/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> isJetDeletable(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;

        try {

            Boolean flag = jetService.getJetIsDeletable(id);
            if(flag)
                result= new GeneralResponse<>(flag, "data is deletable", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(flag, "data is not deletable", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/approvedBy/deleteTable/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> isApprovedByDeletable(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;

        try {

            Boolean flag = adminServcice.getApprovedByDeletable(id);
            if(flag)
                result= new GeneralResponse<>(flag, "data is deletable", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(flag, "data is not deletable", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/department/deleteTable/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> isDepartMentDeletable(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;

        try {

            Boolean flag = adminServcice.getDepartmentIsDelatable(id);
            if(flag)
                result= new GeneralResponse<>(flag, "data is deletable", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(flag, "data is not deletable", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/company/deletable/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> isCompanyDeletable(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;

        try {

            Boolean flag = adminServcice.getCompanyIsDelatable(id);
            if(flag)
                result= new GeneralResponse<>(flag, "data is deletable", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(flag, "data is not deletable", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value="/admin/get/allCompany")
    public ResponseEntity<GeneralResponse<List<Company>>> getAllCompany() throws Exception {

        GeneralResponse<List<Company>> result;

        boolean flag;
        try {

            List<Company> list = adminServcice.getAllCompany();
            if(list.isEmpty())
                result= new GeneralResponse<>(list, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(list, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value="/admin/delete/jet/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteJetById(@PathVariable(name = "id") Long id) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

            Boolean list = jetService.deleteJetMastByJetId(id);
            if(list==false)
                result= new GeneralResponse<>(list, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(list, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    /*@DeleteMapping(value="/admin/delete/department/{id}")
    public GeneralResponse<Boolean> deleteDepartmentById(@PathVariable(name = "id") Long id) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

            Boolean list = adminServcice.deleteDepartmentById(id);
            if(list==false)
                result= new GeneralResponse<>(null, " data not found", false, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(list, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return result;
    }*/

    @DeleteMapping(value="/admin/delete/approved/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteApprovedById(@PathVariable(name = "id") Long id) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {

                Boolean list = adminServcice.deleteApprovedById(id);

                result= new GeneralResponse<>(list, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/admin/add/invoiceSequence/")
    public ResponseEntity<GeneralResponse<Boolean>> addInvoiceSequence(@RequestBody InvoiceSequence record) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {


            if(record == null)
                throw new Exception("null data passed");

            adminServcice.addInvoiceSequence(record);

            result= new GeneralResponse<>(true, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/get/invoiceSequence/")
    public ResponseEntity<GeneralResponse<InvoiceSequence>> getInvoiceSequence() throws Exception {

        GeneralResponse<InvoiceSequence> result=null;

        boolean flag;
        try {


            InvoiceSequence invoiceSequence = adminServcice.getInvoiceSequence();
            if(invoiceSequence!=null)
                result= new GeneralResponse<>(invoiceSequence, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PutMapping(value="/admin/update/invoiceSequence/")
    public ResponseEntity<GeneralResponse<Boolean>> updateInvoiceSequence(@RequestBody InvoiceSequence invoiceSequence) throws Exception {

        GeneralResponse<Boolean> result=null;

        boolean flag;
        try {


            adminServcice.updateInvoiceSequence(invoiceSequence);
            result= new GeneralResponse<>(true, " Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/admin/get/invoiceSequence/{id}")
    public ResponseEntity<GeneralResponse<InvoiceSequence>> getInvoiceSequenceById(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<InvoiceSequence> result=null;

        boolean flag;
        try {
            if(id==null)
                throw new Exception("id can't null");

            InvoiceSequence invoiceSequence = adminServcice.getInvoiceSequenceById(id);
            if(invoiceSequence!=null)
            result= new GeneralResponse<>(invoiceSequence, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(invoiceSequence, " Data not found", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/admin/add/batchSequence/")
    public ResponseEntity<GeneralResponse<Boolean>> addBatchSequence(@RequestBody BatchSequence record) throws Exception {

        GeneralResponse<Boolean> result;

        boolean flag;
        try {


            if(record == null)
                throw new Exception("null data passed");

            adminServcice.addBatchSequence(record);

            result= new GeneralResponse<>(true, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @PutMapping(value="/admin/update/batchSequence/")
    public ResponseEntity<GeneralResponse<BatchSequence>> updateBatchSequence(@RequestBody BatchSequence record) throws Exception {

        GeneralResponse<BatchSequence> result;

        boolean flag;
        try {


            if(record == null)
                throw new Exception("null data passed");

            BatchSequence id = adminServcice.updateBatchSequence(record);

            result= new GeneralResponse<>(id, " Data upadted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/get/batchSequence/")
    public ResponseEntity<GeneralResponse<BatchSequence>> getBatchSequence() throws Exception {

        GeneralResponse<BatchSequence> result;

        boolean flag;
        try {

            BatchSequence batchSequence = adminServcice.getBatchSequence();
            result= new GeneralResponse<>(batchSequence, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/admin/get/batchSequence/{id}")
    public ResponseEntity<GeneralResponse<BatchSequence>> getBatchSequenceById(@PathVariable(name = "id")Long id) throws Exception {

        GeneralResponse<BatchSequence> result;

        boolean flag;
        try {
            if(id==null)
                throw new Exception("null id passed");

            BatchSequence batchSequence = adminServcice.getBatchSequenceById(id);
            result= new GeneralResponse<>(batchSequence, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }




}
