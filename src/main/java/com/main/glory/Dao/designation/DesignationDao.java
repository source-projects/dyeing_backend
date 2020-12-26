package com.main.glory.Dao.designation;

import com.main.glory.model.designation.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DesignationDao extends JpaRepository<Designation,Long> {

    @Query("Select d from Designation d where d.designation != 'Admin'")
    List<Designation> findAllExceptAdmin();

    @Query("select d from Designation d where d.designation='Operator'")
    Designation findDesignationOperator();
}
