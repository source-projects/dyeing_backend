package com.main.glory.servicesImpl;

import com.main.glory.Dao.employee.AttendanceDao;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.request.FilterAttendance;
import com.main.glory.model.employee.request.GetLatestAttendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import com.main.glory.model.employee.response.EmployeeWithAttendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("attendanceServiceImpl")
public class AttendanceServiceImpl {


    @Autowired
    EmployeeServiceImpl employeeService;
    @Autowired
    AttendanceDao attendanceDao;

    public void saveAttendance(Attendance record) {
        attendanceDao.save(record);
    }

    public void updateAttendance(Attendance record) throws Exception {

        Attendance attendanceExist = attendanceDao.getAttendanceById(record.getId());
        /*if(attendanceExist==null)
            throw new Exception("no record found");

        if(attendanceExist.getShift()!=record.getShift())
            throw new Exception("please select right shift");*/

        attendanceDao.saveAndFlush(record);

    }

    public List<Attendance> getAttendanceByEmployeeId(Long id) throws Exception {
        //check the employee exist

        EmployeeMast employeeMast =employeeService.getEmployeeByEmpId(id);

        if(employeeMast==null)
            throw new Exception("no employee found");

        return attendanceDao.getAllAttendanceByEmployeeId(employeeMast.getId());


    }

    public Attendance getAttendanceById(Long id) {
        return  attendanceDao.getAttendanceById(id);
    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeId(Long id) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;
        EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(id);
        if(employeeMastExist==null)
            throw new Exception("no employee record");

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
            List<EmployeeMast> employeeMastList = employeeService.getAllEmployee();
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

            EmployeeMast employeeMastExist = employeeService.getEmployeeById(filterAttendance.getControlId());
            if(employeeMastExist==null)
                throw new Exception("no employee record found");

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
            throw new Exception("no employee record");

        Attendance attendance = attendanceDao.getAttendanceByIdDateAndShift(employeeMastExist.getId(),record.getShift(),record.getDate());

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
}
