package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.BatchReturn;
import com.main.glory.model.StockDataBatchData.response.ReturnBatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatchReturnDao extends JpaRepository<BatchReturn,Long> {

    @Query(value = "select max(x.chl_no) from batch_return as x LIMIT 1",nativeQuery = true)
    Long getlatestChlNo();

    @Query("select new com.main.glory.model.StockDataBatchData.response.ReturnBatchDetail(x.id,x.partyId,x.partyName,x.address,x.partyCode,x.qualityEntryId,x.qualityName,x.qualityId,SUM(x.mtr),x.chlNo,x.challanDate) from BatchReturn x GROUP BY x.chlNo")
    List<ReturnBatchDetail> getAllChallanWithDetail();

    @Query("select x from BatchReturn x where x.chlNo=:chlNo")
    List<BatchReturn> getBatchReturnByChalNo(Long chlNo);

    @Query(value = "select * from batch_return as x where x.chl_no =:chlNo LIMIT 1",nativeQuery = true)
    BatchReturn getChalNoExist(@Param("chlNo") Long chlNo);

    @Query(value = "select new com.main.glory.model.StockDataBatchData.response.ReturnBatchDetail(x.id,x.party_id,x.party_name,x.address,x.party_code,x.quality_entry_id,x.quality_name,x.quality_id,SUM(x.mtr),x.chl_no,x.challan_date) from batch_return as x where x.chl_no=:chlNo LIMTT 1",nativeQuery = true)
    ReturnBatchDetail getChallanDetailByChlNo(@Param("chlNo")Long chlNo);
}
