package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.request.FilterAttendance;
import com.main.glory.model.employee.request.GetLatestAttendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import com.main.glory.model.employee.response.EmployeeWithAttendance;
import com.main.glory.servicesImpl.AttendanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceController extends ControllerConfig {


    @Autowired
    AttendanceServiceImpl attendanceService;


    @PostMapping(value="/attendance")
    public ResponseEntity<GeneralResponse<Boolean>> saveParty(@RequestBody Attendance record)
    {
        GeneralResponse<Boolean> result;
        try {
            if(record==null)
                throw new Exception("record can't be null");


            attendanceService.saveAttendance(record);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            result = new GeneralResponse<Boolean>(true, "Attendance Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PutMapping(value="/attendance")
    public ResponseEntity<GeneralResponse<Boolean>> updateAttendance(@RequestBody Attendance record)
    {
        GeneralResponse<Boolean> result;
        try {
            if(record==null)
                throw new Exception("record can't be null");


            attendanceService.updateAttendance(record);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            result = new GeneralResponse<Boolean>(true, "Date updated Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/attendance/byEmployeeId")
    public ResponseEntity<GeneralResponse<List<Attendance>>> getAttendanceByEmployeeId(@RequestParam(name = "id") Long id)
    {
        GeneralResponse<List<Attendance>> result;
        try {
            if(id==null)
                throw new Exception("record can't be null");


            List<Attendance> list = attendanceService.getAttendanceByEmployeeId(id);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list.isEmpty())
            {result = new GeneralResponse<>(list, "Attendance Data not found ", false, System.currentTimeMillis(), HttpStatus.CREATED);

            }
            else {
                result = new GeneralResponse<>(list, "Date fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/attendance/latest/byEmployeeId")
    public ResponseEntity<GeneralResponse<EmployeeWithAttendance>> getLatestAttendanceByEmployeeId(@RequestParam(name = "id") Long id)
    {
        GeneralResponse<EmployeeWithAttendance> result;
        try {
            if(id==null)
                throw new Exception("record can't be null");


            EmployeeWithAttendance list = attendanceService.getLatestAttendanceRecordByEmployeeId(id);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list==null)
            {
                result = new GeneralResponse<>(list, " Data not found ", false, System.currentTimeMillis(), HttpStatus.CREATED);

            }
            else {
                result = new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PostMapping(value="/attendance/latest/byEmployeeIdDateAndShift")
    public ResponseEntity<GeneralResponse<EmployeeWithAttendance>> getLatestAttendanceByEmployeeIdDateAndShift(@RequestBody GetLatestAttendance record)
    {
        GeneralResponse<EmployeeWithAttendance> result;
        try {
            if(record==null)
                throw new Exception("record can't be null");


            EmployeeWithAttendance list = attendanceService.getLatestAttendanceRecordByEmployeeIdDateAndShift(record);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list==null)
            {
                result = new GeneralResponse<>(list, " Data not found ", false, System.currentTimeMillis(), HttpStatus.CREATED);

            }
            else {
                result = new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/attendance")
    public ResponseEntity<GeneralResponse<Attendance>> getAttendanceById(@RequestParam(name = "id") Long id)
    {
        GeneralResponse<Attendance> result;
        try {
            if(id==null)
                throw new Exception("record can't be null");


            Attendance list = attendanceService.getAttendanceById(id);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list==null)
            {
                result = new GeneralResponse<>(list, "Attendance Data not found ", false, System.currentTimeMillis(), HttpStatus.CREATED);

            }
            else {
                result = new GeneralResponse<>(list, "Attendance Data updated Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //filter api
    @PostMapping(value="/attendance/get/filter")
    public ResponseEntity<GeneralResponse<List<EmployeeAttendanceResponse>>> getEmployeeAttendanceByFilter(@RequestBody FilterAttendance filterAttendance)
    {
        GeneralResponse<List<EmployeeAttendanceResponse>> result;
        try {
            if(filterAttendance==null)
                throw new Exception("record can't be null");


            List<EmployeeAttendanceResponse> list = attendanceService.getAttendanceRecordBasedOnFilter(filterAttendance);
            //System.out.println("har::"+headers.get("id"));
            //System.out.println(id);
            if(list.isEmpty())
            {
                result = new GeneralResponse<>(list, " Data not found ", false, System.currentTimeMillis(), HttpStatus.CREATED);

            }
            else {
                result = new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


}
