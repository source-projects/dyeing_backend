package com.main.glory.Dao.fabric;

import com.main.glory.Lookup.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.model.fabric.FabStockMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface FabStockMastDao extends JpaRepository<FabStockMast, Long> {

	@Query("select new com.main.glory.Lookup.FabInMasterLookUp.MasterLookUpWithRecord(fm.id, fm.stockInType, fm.batch, fm.partyId, fm.billNo, fm.chlNo, fm.billDate, fm.chlDate, fm.lotNo, fm.remark, fm.createdDate, fm.updatedDate, fm.createdBy, fm.updatedBy, fm.userHeadId,(select COUNT(fd.id) from FabStockData fd where fd.controlId = fm.id), (select SUM(fd.mtr) from FabStockData fd where fd.controlId = fm.id), (select SUM(fd.wt) from FabStockData fd where fd.controlId = fm.id)) from FabStockMast fm")
	List<MasterLookUpWithRecord> findAllMasterWithDetails();

}
