package com.main.glory.Dao.admin;


import com.main.glory.model.admin.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentDao extends JpaRepository<Department,Long> {
    @Query("select d from Department d where LOWER(d.name)=LOWER(:name)")
    Department getDepartmentByName(String name);

    @Query("select d from Department d where d.id=:id")
    Department getDepartmentById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Department d where d.id=:id")
    void deleteDepartmentById(Long id);

    @Query("select s from Department s ")
    List<Department> getAllDepartment();
}
