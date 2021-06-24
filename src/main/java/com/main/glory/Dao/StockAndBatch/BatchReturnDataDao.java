package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.BatchReturnData;
import com.main.glory.model.StockDataBatchData.response.ReturnBatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchReturnDataDao extends JpaRepository<BatchReturnData,Long> {


    @Query("select x from BatchReturnData x where x.controlId in (select b.id from BatchReturnMast b where b.chlNo=:chlNo)")
    List<BatchReturnData> getChallanDetailByChlNo(Long chlNo);
}
