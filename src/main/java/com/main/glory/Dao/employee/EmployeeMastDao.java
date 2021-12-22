
package com.main.glory.Dao.employee;

import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.response.GetAllEmployee;
import com.main.glory.model.employee.response.MonthlyAttendanceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface EmployeeMastDao extends JpaRepository<EmployeeMast,Long> {


    @Query("select e from EmployeeMast e where e.aadhaar=:adharNo AND e.id!=:l")
    EmployeeMast getEmployeeByAadhaarExceptId(String adharNo, long l);

    @Query("select s from EmployeeMast s where s.id=:id")
    EmployeeMast getEmployeeById(Long id);

    @Query("select new com.main.glory.model.employee.response.GetAllEmployee(s,(select d.name from Department d where d.id=s.departmentId)) from EmployeeMast s ORDER BY s.id DESC")
    List<GetAllEmployee> getAllEmployee();

    @Modifying
    @Transactional
    @Query("delete from EmployeeMast w where w.id=:id")
    void deleteByEmployeeId(Long id);

    @Query("select x from EmployeeMast x where x.empId=:empId")
    EmployeeMast getEmployeeByEmpId(Long empId);

    @Query("select x from EmployeeMast x where x.name LIKE :id%")
    List<EmployeeMast> getEmployeeByName(String id);

    @Query(value = "select * from employee_mast as x where x.emp_id LIKE :empId% ",nativeQuery = true)
    List<EmployeeMast> getEmployeeByLikeEmpId(@RequestParam("empId") String empId);

    @Query("select new com.main.glory.model.employee.response.MonthlyAttendanceResponse(em.id,em.name,em.empId,ed) from EmployeeMast em INNER JOIN EmployeeData ed on ed.controlId =em.id and ed.type='profile'")
    List<MonthlyAttendanceResponse> getAllEmployeeNameWithId();

   /* @Query("select new com.main.glory.model.employee.response.GetAllEmployee(s," +
            "(select d.name from Department d where d.id=s.departmentId) as name," +
            "(select x from Attendance x where Date(x.inTime)>=Date(:from) AND Date(x.outTime)<=Date(:to) AND x.controlId=s.id) as records" +
            ") from EmployeeMast s ORDER BY s.id DESC")
    List<GetAllEmployee> getAllEmployeeWithDateFilter(Date from, Date to);*/

  /* @Query("select new com.main.glory.model.employee.response.GetAllEmployee(s,(select d.name from Department d where d.id=s.departmentId),(select x from Attendance x where Date(x.inTime)>=Date(:from) AND Date(x.outTime)<=Date(:to) AND x.controlId=s.id)) from EmployeeMast s ORDER BY s.id DESC")
   List<GetAllEmployee> getAllEmployeeWithDateFilter(Date from,Date to);*/
}
