package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.employee.EmployeeData;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.request.AddEmployee;
import com.main.glory.servicesImpl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController extends ControllerConfig {


    @Autowired
    EmployeeServiceImpl employeeService;

    @PostMapping(value = "/employee/add")
    public ResponseEntity<GeneralResponse<Long>> addEmployee(@RequestBody EmployeeMast record) throws Exception {

        GeneralResponse<Long> result;
        boolean flag;
        try {
            if(record == null)
                throw new Exception("null data passed");


            Long id= employeeService.addEmployeeRecord(record);

            result= new GeneralResponse<>(id, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PostMapping(value = "/employee/add/document")
    public ResponseEntity<GeneralResponse<Long>> addEmployeeDocument(@RequestBody EmployeeData record) throws Exception {

        GeneralResponse<Long> result;
        boolean flag;
        try {
            if(record == null)
                throw new Exception("null data passed");


            Long id= employeeService.addEmployeeDataRecord(record);

            result= new GeneralResponse<>(id, " Data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value = "/employee/update")
    public ResponseEntity<GeneralResponse<Long>> updateEmployee(@RequestBody EmployeeMast record) throws Exception {

        GeneralResponse<Long> result;
        boolean flag;
        try {
            if(record == null)
                throw new Exception("null data passed");


            Long id= employeeService.updateEmployeeRecord(record);

            result= new GeneralResponse<>(id, " Data updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/employee")
    public ResponseEntity<GeneralResponse<EmployeeMast>> getEmployeeById(@RequestParam(name = "id") Long id) throws Exception {

        GeneralResponse<EmployeeMast> result;
        boolean flag;
        try {
            if(id == null)
                throw new Exception("null data passed");


            EmployeeMast employeeMast= employeeService.getEmployeeById(id);

            if(employeeMast!=null)
            result= new GeneralResponse<>(employeeMast, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
            result= new GeneralResponse<>(employeeMast, " Data not found", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/employee/all")
    public ResponseEntity<GeneralResponse<List<EmployeeMast>>> getEmployeAll() throws Exception {

        GeneralResponse<List<EmployeeMast>> result;
        boolean flag;
        try {

            List<EmployeeMast> employeeMast= employeeService.getAllEmployee();

            if(employeeMast.size()>0)
                result= new GeneralResponse<>(employeeMast, " Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                result= new GeneralResponse<>(employeeMast, " Data not found", false, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping(value = "/employee")
    public ResponseEntity<GeneralResponse<Boolean>> deleteEmployeeById(@RequestParam(name = "id")Long id) throws Exception {

        GeneralResponse<Boolean> result;
        boolean flag;
        try {
            if(id==null)
                throw new Exception("null id passed");

            employeeService.deleteEmployeeById(id);
            result= new GeneralResponse<>(true, " Data deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

   /* @PostMapping(value = "/employee/file")
    public ResponseEntity<GeneralResponse<Boolean>> addfile(@RequestBody MultipartFile id) throws Exception {

        GeneralResponse<Boolean> result;
        boolean flag;
        try {
            if(id==null)
                throw new Exception("null id passed");

            //employeeService.deleteEmployeeById(id);
            //String filePath ="/path/"+id;
            //String filename = getRandomString();

            //File targetFile = new File(id.getOriginalFilename());

            File parent = new File("path");
            if(!parent.exists())
                parent.mkdir();

            id.transferTo(new File(parent+"/"+id.getOriginalFilename()));
            //e.setUrl(filePath);
            //System.out.println(targetFile.getAbsoluteFile());
            result= new GeneralResponse<>(true, " Data created successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
*/
}
