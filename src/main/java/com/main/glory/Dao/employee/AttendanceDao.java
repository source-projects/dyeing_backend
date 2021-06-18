package com.main.glory.Dao.employee;


import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import com.main.glory.model.employee.response.MonthlyAttendanceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface AttendanceDao extends JpaRepository<Attendance,Long> {

    @Query("select s from Attendance s where s.controlId=:empId")
    List<Attendance> getAllAttendanceByEmployeeId(Long empId);

    @Query("select s from Attendance s where s.id=:id")
    Attendance getAttendanceById(Long id);

    @Query(value = "select * from attendance as a where a.control_id = :id AND shift=false ORDER BY a.created_date DESC LIMIT 1",nativeQuery = true)
    Attendance getLatestAttendanceRecordByEmployeeId(@RequestParam(name = "id") Long id);

    @Query("select new com.main.glory.model.employee.response.EmployeeAttendanceResponse(a.shift,sum(a.id)) from Attendance a where a.controlId=:id AND a.createdDate >=:fromDate OR a.createdDate <= :toDate GROUP BY a.shift")
    EmployeeAttendanceResponse getAttendanceBasedOnFilter(Long id, Date fromDate, Date toDate);

    @Query(value = "select * from attendance as a where a.control_id=:controlId and a.shift=:shift ORDER BY Date(a.created_date)=Date(GETDATE()) LIMIT 1",nativeQuery = true)
    Attendance getAttendanceByIdAndShift(@RequestParam("controlId") Long controlId, @RequestParam("shift") Boolean shift);

    @Query(value = "select * from attendance as a where a.control_id=:id and a.shift=:shift AND Date(a.in_time)=Date(:date) LIMIT 1",nativeQuery = true)
    Attendance getAttendanceByIdDateAndShift(@RequestParam("id") Long id,@RequestParam("shift") Boolean shift,@RequestParam("date") Date date);

    @Query("select x from Attendance x where Date(x.inTime)>=Date(:from) AND Date(x.outTime)<=Date(:to) AND x.controlId=:id")
    List<Attendance> getAllAttendanceByControlIdIdWithDate(Long id, Date from, Date to);

    @Query(value = "select * from attendance as x where x.control_id=:id AND Date(x.in_time)=Date(:date) ORDER by x.id DESC LIMIT 1",nativeQuery = true)
    Attendance getAttendanceByDateAndIdDescendingOrder(@RequestParam("date") Date date, @RequestParam("id") Long id);

    //@Query("select new com.main.glory.model.employee.response.MonthlyAttendanceResponse((select x from EmployeeMast x where x.id=a.controlId),select COUNT(aa.approved) from Attendance aa where DATE(aa.inTime) >= DATE(:from) AND DATE(aa.intTime) <= DATE(:to) AND aa.approved=true,select COUNT(aa.approved) from Attendance aa where DATE(aa.inTime) >= DATE(:from) AND DATE(aa.intTime) <= DATE(:to) AND aa.approved=false) from Attendance a")
    //List<MonthlyAttendanceResponse> getMonthlyResponseByDateFilter(Date from, Date to);

    @Query("select count(x.id) from Attendance x where DATE(x.inTime) >=DATE(:from) AND DATE(x.inTime)<=DATE(:to) AND x.approved=:flag AND x.controlId=:id")
    Long getApprovedFlagAttendanceByIdAndDateFilter(Date from,Date to,Long id,Boolean flag);
}
