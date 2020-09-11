package com.main.glory.Dao;

import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.main.glory.model.Fabric;
import java.util.List;


@EnableJpaRepositories
public interface FabricsDao extends JpaRepository<Fabric, Long> {
    @Query(value = ("SELECT distinct ms.*,count(*) as recordCount FROM fabric_master as ms inner join fabstock as fs on ms.id=fs.control_id WHERE fs.is_active = 1 group by ms.id"), nativeQuery = true)
    List<MasterLookUpWithRecord> getFabStockMasterRecordList();

}
