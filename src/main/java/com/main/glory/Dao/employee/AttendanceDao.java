package com.main.glory.Dao.employee;

import com.main.glory.model.employee.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceDao extends JpaRepository<Attendance,Long> {

    @Query("select s from Attendance s where s.controlId=:empId ")
    List<Attendance> getAllAttendanceByEmployeeId(Long empId);

    @Query("select s from Attendance s where s.id=:id")
    Attendance getAttendanceById(Long id);
}
