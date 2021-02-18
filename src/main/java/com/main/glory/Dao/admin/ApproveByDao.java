package com.main.glory.Dao.admin;

import com.main.glory.model.admin.ApprovedBy;
import com.main.glory.model.admin.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApproveByDao extends JpaRepository<ApprovedBy,Long> {

    @Query("select x from ApprovedBy x where LOWER(x.name)=LOWER(:name)")
    ApprovedBy findByApprovedByName(String name);

    @Query("select q from ApprovedBy q")
    List<ApprovedBy> getAll();

    @Query("select x from ApprovedBy x where x.id=:id")
    ApprovedBy getApprovedById(Long id);

    @Transactional
    @Modifying
    @Query("delete from ApprovedBy x where x.id=:id")
    void deleteApprovedById(Long id);

}
