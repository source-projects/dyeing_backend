package com.main.glory.Dao;

import com.main.glory.model.supplier.SupplierRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface SupplierRateDao extends JpaRepository<SupplierRate, Long> {

    @Transactional
    @Modifying
    @Query("update SupplierRate sr set sr.isActive = 0 where sr.supplierId = :cid")
    public void setInactive(@Param("cid") Long control_id);


    List<SupplierRate> findBySupplierIdAndIsActive(Long aLong, Boolean aBoolean);
}
