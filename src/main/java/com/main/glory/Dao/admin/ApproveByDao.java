package com.main.glory.Dao.admin;

import com.main.glory.model.admin.ApprovedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApproveByDao extends JpaRepository<ApprovedBy,Long> {

    @Query("select x from ApprovedBy x where LOWER(x.name)=LOWER(:name)")
    ApprovedBy findByApprovedByName(String name);

    @Query("select q from ApprovedBy q")
    List<ApprovedBy> getAll();
}
