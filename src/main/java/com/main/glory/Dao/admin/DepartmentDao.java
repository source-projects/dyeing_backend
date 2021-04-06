package com.main.glory.Dao.admin;


import com.main.glory.model.admin.Department;
import com.main.glory.model.admin.request.DepartmentResponse;
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

    @Query("select new com.main.glory.model.admin.request.DepartmentResponse(d,(select u.id from UserData u where u.departmentId=d.id AND u.id=u.userHeadId)) from Department d")
    List<DepartmentResponse> getDepartmentResponse();

    @Query("select new com.main.glory.model.admin.request.DepartmentResponse(d,(select u.id from UserData u where u.departmentId=d.id AND u.id=:userId)) from Department d where d.id = (select u.departmentId from UserData u where u.id=:userId)")
    List<DepartmentResponse> getDepartmentResponseByUserId(Long userId);

    @Query("select new com.main.glory.model.admin.request.DepartmentResponse(d,(select u.id from UserData u where u.departmentId=:id AND u.id=u.userHeadId)) from Department d where d=:id")
    DepartmentResponse getDepartmentResponseById(Long id);
}
