package com.main.glory.Dao.designation;

import com.main.glory.model.designation.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DesignationDao extends JpaRepository<Designation,Long> {

    @Query("Select d from Designation d where d.designation != 'Admin'")
    List<Designation> findAllExceptAdmin();

    @Query("select d from Designation d where d.designation='Operator'")
    Designation findDesignationOperator();

    @Modifying
    @Transactional
    @Query("delete from Designation d where d.id=:id")
    void deleteDesignationById(Long id);

    @Query("select s from Designation s where s.id=:id")
    Designation getDesignationById(Long id);
}
