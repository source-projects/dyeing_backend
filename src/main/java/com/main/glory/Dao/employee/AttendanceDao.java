package com.main.glory.Dao.employee;


import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface AttendanceDao extends JpaRepository<Attendance,Long> {

    @Query("select s from Attendance s where s.controlId=:empId ")
    List<Attendance> getAllAttendanceByEmployeeId(Long empId);

    @Query("select s from Attendance s where s.id=:id")
    Attendance getAttendanceById(Long id);

    @Query(value = "select * from attendance as a where a.control_id=:id ORDER BY a.created_date DESC LIMIT 1",nativeQuery = true)
    Attendance getLatestAttendanceRecordByEmployeeId(@RequestParam(name = "id") Long id);

    @Query("select new com.main.glory.model.employee.response.EmployeeAttendanceResponse(a.shift,sum(a.id)) from Attendance a where a.controlId=:id AND a.createdDate >=:fromDate OR a.createdDate <= :toDate GROUP BY a.shift")
    EmployeeAttendanceResponse getAttendanceBasedOnFilter(Long id, Date fromDate, Date toDate);
}
