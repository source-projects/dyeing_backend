package com.main.glory.Dao;

import com.main.glory.model.supplier.SupplierRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRateDao extends JpaRepository<SupplierRate, Long> {
    @Query(value = "UPDATE supplier_rate SET is_active = 0 where control_id = :c_id", nativeQuery = true)
    void setInactive(@Param("c_id") Long control_id);

    List<SupplierRate> findBySupplierIdAndIsActive(Long aLong, Boolean aBoolean);
}
