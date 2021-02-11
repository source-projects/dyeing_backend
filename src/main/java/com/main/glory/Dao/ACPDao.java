package com.main.glory.Dao;

import com.main.glory.model.shade.ACP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ACPDao extends JpaRepository<ACP,Long> {


    @Query("select MAX(a.postFix) from ACP a")
    Long getAcpNumber();

    @Query("select x from ACP x where x.postFix=:data")
    ACP getAcpNumberExist(Long data);
}
