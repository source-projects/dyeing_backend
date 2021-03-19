package com.main.glory.servicesImpl;

import com.main.glory.Dao.employee.AttendanceDao;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
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
}
