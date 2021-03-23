package com.main.glory.servicesImpl;

import com.main.glory.Dao.employee.AttendanceDao;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.responce.EmployeeWithAttendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if(attendanceExist==null)
            throw new Exception("no record found");

        if(attendanceExist.getShift()!=record.getShift())
            throw new Exception("please select right shift");

        attendanceDao.saveAndFlush(record);

    }

    public List<Attendance> getAttendanceByEmployeeId(Long id) throws Exception {
        //check the employee exist

        EmployeeMast employeeMast =employeeService.getEmployeeById(id);

        if(employeeMast==null)
            throw new Exception("no employee found");

        return attendanceDao.getAllAttendanceByEmployeeId(id);


    }

    public Attendance getAttendanceById(Long id) {
        return  attendanceDao.getAttendanceById(id);
    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeId(Long id) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;
        EmployeeMast employeeMastExist = employeeService.getEmployeeById(id);
        if(employeeMastExist==null)
            throw new Exception("no employee record");

        Attendance attendance = attendanceDao.getLatestAttendanceRecordByEmployeeId(id);

        if(attendance==null)//attendance.getInTime()!=null && attendance.getOutTime()!=null)
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
