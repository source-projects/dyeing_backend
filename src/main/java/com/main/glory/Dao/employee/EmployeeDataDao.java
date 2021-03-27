package com.main.glory.Dao.employee;

import com.main.glory.model.employee.EmployeeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeDataDao extends JpaRepository<EmployeeData,Long> {


    @Query("select x from EmployeeData x where x.controlId=:id")
    List<EmployeeData> getEmployeeDataByEmployeeId(Long id);
}
