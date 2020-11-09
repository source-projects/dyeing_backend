package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BatchDao extends  JpaRepository<Batch, Long> {
}

