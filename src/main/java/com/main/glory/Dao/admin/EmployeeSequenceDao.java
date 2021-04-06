package com.main.glory.Dao.admin;

import com.main.glory.model.admin.EmployeeSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeSequenceDao extends JpaRepository<EmployeeSequence,Long> {

    @Query(value = "select * from employee_sequence ORDER BY id DESC LIMIT 1",nativeQuery = true)
    EmployeeSequence getEmployeeSequence();
}
