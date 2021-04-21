package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.request.FilterAttendance;
import com.main.glory.model.employee.request.GetLatestAttendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import com.main.glory.model.employee.response.EmployeeWithAttendance;
import com.main.glory.servicesImpl.AttendanceServiceImpl;
import com.main.glory.servicesImpl.EmployeeServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ObjectName;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceController extends ControllerConfig {

    CommonMessage commonMessage;

    @Autowired
    EmployeeServiceImpl employeeService;

    @Autowired
    LogServiceImpl logService;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @Autowired
    HttpServletRequest request;

    @Autowired
    AttendanceServiceImpl attendanceService;


    @PostMapping(value="/attendance")
    public ResponseEntity<GeneralResponse<Long,Object>> saveParty(@RequestBody Attendance record)
    {
        GeneralResponse<Long,Object> result;
        try {
            if(record==null)
                throw new Exception(commonMessage.Null_Record_Passed);

            Attendance attendance= attendanceService.saveAttendance(record);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);

            result = new GeneralResponse<>(attendance.getId(), commonMessage.Attendance_Added, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        }
        catch (Exception e)
        {
            e.printStackTrace();

            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value="/attendance")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateAttendance(@RequestBody Attendance record)
    {
        GeneralResponse<Boolean,Object> result;
        try {
            if(record==null)
                throw new Exception(commonMessage.Null_Record_Passed);


            Attendance x = attendanceService.updateAttendance(record);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);

            result = new GeneralResponse<>(true, commonMessage.Attendance_Updated, true, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,debugAll);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/attendance/byEmployeeId")
    public ResponseEntity<GeneralResponse<List<Attendance>, Object>> getAttendanceByEmployeeId(@RequestParam(name = "id") Long id)
    {
        GeneralResponse<List<Attendance>,Object> result;
        try {
            if(id==null)
                throw new Exception(commonMessage.Null_Record_Passed);


            List<Attendance> list = attendanceService.getAttendanceByEmployeeId(id);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list.isEmpty())
            {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            }
            else {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }

            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/attendance/latest/byEmployeeId")
    public ResponseEntity<GeneralResponse<EmployeeWithAttendance,Object>> getLatestAttendanceByEmployeeId(@RequestParam(name = "id") Long id)
    {
        GeneralResponse<EmployeeWithAttendance,Object> result;
        try {
            if(id==null)
                throw new Exception(commonMessage.Null_Record_Passed);


            EmployeeWithAttendance list = attendanceService.getLatestAttendanceRecordByEmployeeId(id);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list==null)
            {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            }
            else {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PostMapping(value="/attendance/latest/byEmployeeIdDateAndShift")
    public ResponseEntity<GeneralResponse<EmployeeWithAttendance,Object>> getLatestAttendanceByEmployeeIdDateAndShift(@RequestBody GetLatestAttendance record)
    {
        GeneralResponse<EmployeeWithAttendance,Object> result;
        try {
            if(record==null)
                throw new Exception(commonMessage.Null_Record_Passed);


            EmployeeWithAttendance list = attendanceService.getLatestAttendanceRecordByEmployeeIdDateAndShift(record);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list==null)
            {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,record);

            }
            else {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);
            }


            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,record);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/attendance")
    public ResponseEntity<GeneralResponse<Attendance,Object>> getAttendanceById(@RequestParam(name = "id") Long id)
    {
        GeneralResponse<Attendance,Object> result;
        try {
            if(id==null)
                throw new Exception(commonMessage.Null_Record_Passed);


            Attendance list = attendanceService.getAttendanceById(id);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list==null)
            {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            }
            else {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Updated, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //filter api
    @PostMapping(value="/attendance/get/filter")
    public ResponseEntity<GeneralResponse<List<EmployeeAttendanceResponse>,Object>> getEmployeeAttendanceByFilter(@RequestBody FilterAttendance filterAttendance)
    {
        GeneralResponse<List<EmployeeAttendanceResponse>,Object> result;
        try {
            if(filterAttendance==null)
                throw new Exception(commonMessage.Null_Record_Passed);


            List<EmployeeAttendanceResponse> list = attendanceService.getAttendanceRecordBasedOnFilter(filterAttendance);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list.isEmpty())
            {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,filterAttendance);

            }
            else {
                result = new GeneralResponse<>(list, commonMessage.Attendance_Found, true, System.currentTimeMillis(), HttpStatus.OK,filterAttendance);
            }

            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,filterAttendance);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


}
