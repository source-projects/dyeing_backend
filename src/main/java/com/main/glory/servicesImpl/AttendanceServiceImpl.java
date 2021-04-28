package com.main.glory.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.glory.Dao.employee.AttendanceDao;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.request.FilterAttendance;
import com.main.glory.model.employee.request.GetLatestAttendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import com.main.glory.model.employee.response.EmployeeWithAttendance;
import com.main.glory.model.employee.response.GetAllEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service("attendanceServiceImpl")
public class AttendanceServiceImpl {

    /*

        * always get the record entry id by EmpId coming from FE and then map entry with child record attendance record


     */

    CommonMessage commonMessage;

    @Autowired
    EmployeeServiceImpl employeeService;
    @Autowired
    AttendanceDao attendanceDao;

    public Attendance saveAttendance(Attendance record) throws Exception {
        EmployeeMast employeeMast  =employeeService.getEmployeeByEmpId(record.getControlId());
        if(employeeMast==null)
            throw new Exception(commonMessage.Employee_Not_Found);

        record.setControlId(employeeMast.getId());
        Attendance attendance = attendanceDao.save(record);
        return attendance;
    }

    public Attendance updateAttendance(Attendance record) throws Exception {

        EmployeeMast employeeMast  =employeeService.getEmployeeByEmpId(record.getControlId());
        if(employeeMast==null)
            throw new Exception(commonMessage.Employee_Not_Found);

        record.setControlId(employeeMast.getId());

        Attendance attendanceExist = attendanceDao.getAttendanceById(record.getId());
        /*if(attendanceExist==null)
            throw new Exception("no record found");

        if(attendanceExist.getShift()!=record.getShift())
            throw new Exception("please select right shift");*/

        Attendance x= attendanceDao.saveAndFlush(record);
        return x;

    }

    public List<Attendance> getAttendanceByEmployeeId(Long id) throws Exception {
        //check the employee exist

        EmployeeMast employeeMast =employeeService.getEmployeeByEmpId(id);

        if(employeeMast==null)
            throw new Exception(commonMessage.Employee_Not_Found);

        return attendanceDao.getAllAttendanceByEmployeeId(employeeMast.getId());


    }

    public Attendance getAttendanceById(Long id) {
        return  attendanceDao.getAttendanceById(id);
    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeId(Long id) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;
        EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(id);
        if(employeeMastExist==null)
            throw new Exception(commonMessage.Employee_Not_Exist);

        Attendance attendance = attendanceDao.getLatestAttendanceRecordByEmployeeId(employeeMastExist.getId());

        if(attendance==null || (attendance.getInTime()!=null && attendance.getOutTime()!=null))
        {
            employeeWithAttendance =new EmployeeWithAttendance(employeeMastExist,new Attendance());
        }
        else
        {
            employeeWithAttendance=new EmployeeWithAttendance(employeeMastExist,attendance);
        }
        return  employeeWithAttendance;

    }

    public List<EmployeeAttendanceResponse> getAttendanceRecordBasedOnFilter(FilterAttendance filterAttendance) throws Exception {
        List<EmployeeAttendanceResponse> list=new ArrayList<>();




        if(filterAttendance.getControlId()==null)
        {
            List<GetAllEmployee> employeeMastList = employeeService.getAllEmployee();
            for(EmployeeMast employeeMast:employeeMastList)
            {
                EmployeeAttendanceResponse employeeAttendanceResponse = attendanceDao.getAttendanceBasedOnFilter(employeeMast.getId(),filterAttendance.getFromDate(),filterAttendance.getToDate());
                if(employeeAttendanceResponse!=null)
                list.add(new EmployeeAttendanceResponse(employeeAttendanceResponse,filterAttendance,employeeMast));
            }
        }
        else
        {
            //check the employee is exist or not

            EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(filterAttendance.getControlId());
            if(employeeMastExist==null)
                throw new Exception(commonMessage.Employee_Not_Found);

            EmployeeAttendanceResponse employeeAttendanceResponse = attendanceDao.getAttendanceBasedOnFilter(employeeMastExist.getId(),filterAttendance.getFromDate(),filterAttendance.getToDate());
            if(employeeAttendanceResponse!=null)
                list.add(new EmployeeAttendanceResponse(employeeAttendanceResponse,filterAttendance,employeeMastExist));

        }

        return list;
    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeIdDateAndShift(GetLatestAttendance record) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;
        EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(record.getId());
        if(employeeMastExist==null)
            throw new Exception(commonMessage.Employee_Not_Exist);


       /* if(record.getShift()==false)
        {
            Calendar cal =Calendar.getInstance();
            cal.setTime(record.getDate());
            cal.add(Calendar.DATE,-1);
            record.setDate(cal.getTime());
        }*/
        Attendance attendance = attendanceDao.getAttendanceByIdDateAndShift(employeeMastExist.getId(),record.getShift(),record.getDate());

        ObjectMapper objectMapper =new ObjectMapper();
        if(attendance==null || (attendance.getInTime()!=null && attendance.getOutTime()!=null))
        {
            attendance  = new Attendance();
            System.out.println(objectMapper.writeValueAsString(attendance));
            employeeWithAttendance =new EmployeeWithAttendance(employeeMastExist,attendance);
        }
        else
        {
            System.out.println(objectMapper.writeValueAsString(attendance));
            employeeWithAttendance=new EmployeeWithAttendance(employeeMastExist,attendance);
        }
        System.out.println(objectMapper.writeValueAsString(employeeWithAttendance));
        return  employeeWithAttendance;
    }
}
