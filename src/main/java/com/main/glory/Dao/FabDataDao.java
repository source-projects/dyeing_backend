package com.main.glory.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.main.glory.model.FabricInRecord;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FabDataDao extends JpaRepository<FabricInRecord, Long> {

    @Query(value = "SELECT * FROM fabstock as p WHERE p.control_id = :pparty_id and is_active=1", nativeQuery = true)
    List<FabricInRecord> getAllFabStockById(@Param("pparty_id") Long id);

    @Modifying
    @Query(value = "UPDATE fabstock SET is_active = '0' WHERE control_id = :mid", nativeQuery = true)
    void setisDeActive(@Param("mid") Long fabmasterId);

}
