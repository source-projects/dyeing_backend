package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BatchDao extends  JpaRepository<BatchData, Long> {


    List<BatchData> findByControlId(Long controlId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse(SUM(p.wt) as WT, SUM(p.mtr) as MTR, p.batchId as batchId) from BatchData p where p.controlId =:id AND isProductionPlanned = false    GROUP BY p.batchId ")
    List<GetAllBatchResponse> findAllQTYControlId(@Param("id") Long id);

    Optional<List<BatchData>> findByBatchId(String batchId);
}

