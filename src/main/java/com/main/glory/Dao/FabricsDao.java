package com.main.glory.Dao;

import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.main.glory.model.Fabric;
import java.util.List;


@EnableJpaRepositories
public interface FabricsDao extends JpaRepository<Fabric, Long> {
    @Query(value =("select count(fs.id) as record_count , (select p.party_name from party as p where p.entry_id=fm.party_id) as party_name,fm.id, fm.batch, fm.bill_date, fm.bill_id, fm.bill_no, fm.chl_date, fm.chl_no, fm.created_by, fm.created_date, fm.date, fm.lot_no, fm.party_id, fm.remark, fm.stock_id, fm.stock_in_type, fm.updated_by, fm.updated_date, fm.user_head_id, fm.record_count from fabric_master as fm \n" +
            "inner join fabstock as fs on fm.id=fs.control_id where fs.is_active='1' GROUP by fm.party_name   "),nativeQuery = true)
   List<MasterLookUpWithRecord> getFabStockMasterRecordList();
}
