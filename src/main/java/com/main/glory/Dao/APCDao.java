package com.main.glory.Dao;

import com.main.glory.model.shade.APC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface APCDao extends JpaRepository<APC,Long> {


    @Query("select MAX(a.postFix) from APC a")
    Long getAcpNumber();

    @Query("select x from APC x where x.postFix=:data")
    APC getAcpNumberExist(Long data);
}
